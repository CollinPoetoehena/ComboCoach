# Deployment Guide

TODO: extend this a bit and use the latest version of the code/setup for this in the future.

## Production Build

```bash
./gradlew jsBrowserProductionWebpack
# Output: app/build/dist/js/productionExecutable/
```

## Deploy to Vercel (Recommended)

```bash
npm install -g vercel
./gradlew jsBrowserProductionWebpack
vercel --prod
```

Or connect GitHub repo at https://vercel.com/new

## Deploy to Netlify

```bash
npm install -g netlify-cli
./gradlew jsBrowserProductionWebpack
netlify deploy --prod --dir=app/build/dist/js/productionExecutable
```

## Deploy to GitHub Pages

Create `.github/workflows/pages.yml`:

```yaml
name: Deploy
on:
  push:
    branches: [main]
jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-java@v3
        with:
          java-version: '17'
      - run: ./gradlew jsBrowserProductionWebpack
      - uses: actions/upload-pages-artifact@v2
        with:
          path: app/build/dist/js/productionExecutable
      - uses: actions/deploy-pages@v2
```

## Docker

```dockerfile
FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY . .
RUN ./gradlew jsBrowserProductionWebpack

FROM nginx:alpine
COPY --from=build /app/app/build/dist/js/productionExecutable /usr/share/nginx/html
EXPOSE 80
```

```bash
docker run -p 8080:80 combocoach
```