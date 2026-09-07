# Security Review

**Date:** 2026-09-07  
**Scope:** Repository, backend configuration, API error handling.

## Checklist

| Check | Status | Evidence |
|-------|--------|----------|
| No secrets in source control | PASS | `.env` gitignored; `.env.example` has no real credentials |
| No hardcoded DB passwords | PASS (fixed) | Removed `root@123` default from `application.yml` |
| Credentials via environment | PASS | `DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD` |
| No stack traces in API responses | PASS | `GlobalExceptionHandler` returns structured errors only |
| No auth scope (per spec) | N/A | Authentication explicitly out of scope |
| SQL injection mitigation | PASS | JPA parameterized queries |
| Frontend does not store secrets | PASS | No API keys in frontend code |

## Finding Remediated

**Hardcoded password default in `application.yml`**

- **Before:** `password: ${DATABASE_PASSWORD:root@123}`
- **After:** `password: ${DATABASE_PASSWORD:}`
- **Risk:** Low (local dev default only), but violated NFR-005 / AC-020
- **Action:** Default removed; developers must set `DATABASE_PASSWORD` via environment or local config

## Recommendations

1. Use `application-local.yml` (already gitignored) for local PostgreSQL credentials.
2. Do not commit `.env` files; use `backend/.env.example` as template only.
3. When deploying, inject secrets via environment or a secrets manager.

## Conclusion

Security posture is **appropriate for the assignment scope**. No authentication is required per specification. Secret handling complies after the `application.yml` fix.
