#!/usr/bin/env bash
set -euo pipefail

PROMETHEUS_URL="${PROMETHEUS_URL:-http://localhost:9090}"
GRAFANA_URL="${GRAFANA_URL:-http://localhost:3000}"
LOKI_URL="${LOKI_URL:-http://localhost:3100}"
TEMPO_URL="${TEMPO_URL:-http://localhost:3200}"
OTEL_COLLECTOR_HOST="${OTEL_COLLECTOR_HOST:-localhost}"
CHECK_SERVICE_TARGETS="${CHECK_SERVICE_TARGETS:-true}"

require_command() {
  if ! command -v "$1" >/dev/null 2>&1; then
    echo "Missing required command: $1" >&2
    exit 1
  fi
}

check_http() {
  local name="$1"
  local url="$2"
  local expected="$3"

  if ! curl --fail --silent --show-error "$url" | grep -q "$expected"; then
    echo "FAILED: $name is not healthy at $url" >&2
    exit 1
  fi

  echo "OK: $name"
}

check_tcp() {
  local name="$1"
  local host="$2"
  local port="$3"

  if ! timeout 3 bash -c "</dev/tcp/$host/$port" >/dev/null 2>&1; then
    echo "FAILED: $name is not accepting TCP connections on $host:$port" >&2
    exit 1
  fi

  echo "OK: $name"
}

check_prometheus_target() {
  local job="$1"
  local query_url
  query_url="$PROMETHEUS_URL/api/v1/query?query=up%7Bjob%3D%22${job}%22%7D"

  if ! curl --fail --silent --show-error "$query_url" | grep -q '"value":\[[^]]*,"1"\]'; then
    echo "FAILED: Prometheus target is down or missing: $job" >&2
    exit 1
  fi

  echo "OK: Prometheus target $job"
}

require_command curl
require_command timeout

check_http "Prometheus" "$PROMETHEUS_URL/-/ready" "Prometheus Server is Ready"
check_http "Grafana" "$GRAFANA_URL/api/health" "ok"
check_http "Loki" "$LOKI_URL/ready" "ready"
check_http "Tempo" "$TEMPO_URL/ready" "ready"
check_tcp "OpenTelemetry Collector gRPC" "$OTEL_COLLECTOR_HOST" "4317"
check_tcp "OpenTelemetry Collector HTTP" "$OTEL_COLLECTOR_HOST" "4318"

if [[ "$CHECK_SERVICE_TARGETS" == "true" ]]; then
  check_prometheus_target "api-gateway"
  check_prometheus_target "user-service"
  check_prometheus_target "ticket-service"
  check_prometheus_target "event-service"
  check_prometheus_target "payment-service"
  check_prometheus_target "notification-service"
  check_prometheus_target "importer-service"
fi

echo "Observability smoke check passed."
