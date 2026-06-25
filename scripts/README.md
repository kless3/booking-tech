# Scripts

This directory contains local helper scripts for development, smoke checks, and manual verification.

## `keycloak-token.sh`

Prints a Keycloak access token for a local user.

```bash
./scripts/keycloak-token.sh <username> <password>
```

Optional environment overrides:

- `KEYCLOAK_URL`
- `KEYCLOAK_REALM`
- `KEYCLOAK_CLIENT_ID`
- `KEYCLOAK_USERNAME`
- `KEYCLOAK_PASSWORD`

## `e2e-purchase-flow.sh`

Runs a black-box booking flow through the public API gateway.

```bash
export KEYCLOAK_ADMIN_PASSWORD='<local keycloak admin password>'
export E2E_USER_PASSWORD='<temporary e2e user password>'

./scripts/e2e-purchase-flow.sh
```

The script creates throwaway users, creates a bookable event, reserves a ticket, captures payment, checks receipt generation, and verifies notification delivery.

## `observability-smoke.sh`

Checks the local observability stack and, by default, Prometheus scrape targets for all services.

```bash
./scripts/observability-smoke.sh
```

To verify only the observability infrastructure:

```bash
CHECK_SERVICE_TARGETS=false ./scripts/observability-smoke.sh
```

Optional environment overrides:

- `PROMETHEUS_URL`
- `GRAFANA_URL`
- `LOKI_URL`
- `TEMPO_URL`
- `OTEL_COLLECTOR_HOST`
- `CHECK_SERVICE_TARGETS`
