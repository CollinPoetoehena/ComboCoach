# Deployment & GitHub Actions CI/CD Pipeline Setup

This repository uses GitHub Actions for automated testing, building, and deployment.

## Pipeline Overview

The pipeline consists of three jobs:

### 1. **Test** (Runs on all branches)
- Runs on every push to any branch
- Runs on all pull requests to `main`
- Executes tests using Node.js

### 2. **Build & Deploy** (Runs only on main branch)
- Only runs after tests pass on `main` branch
- Compiles Kotlin/JS to optimized production JavaScript bundle
- Deploys production bundle to Vercel
- Requires Vercel secrets to be configured
- See for details about GitHub Actions with Vercel: https://vercel.com/docs/git/vercel-for-github#using-github-actions and https://vercel.com/kb/guide/how-can-i-use-github-actions-with-vercel

## Required GitHub Secrets

To enable Vercel deployment, add these secrets to your GitHub repository:

### How to Get Vercel Credentials & Link to GitHub Project
Follow this guide on how to get Vercel credentials and link your GitHub project: https://vercel.com/kb/guide/how-can-i-use-github-actions-with-vercel#configuring-github-actions-for-vercel

Add the created secrets mentioned in the above link to GitHub: https://docs.github.com/en/actions/how-tos/write-workflows/choose-what-workflows-do/use-secrets

## Local Testing

Before pushing, you can test locally:

```bash
# Run tests (what GitHub Actions runs)
./gradlew clean jsNodeTest

# Build production bundle (what GitHub Actions builds)
./gradlew jsBrowserProductionWebpack

# Deploy manually to Vercel
vercel --prod
```

## Monitoring Pipeline

### View Pipeline Status
- Go to your repository on GitHub
- Click the **Actions** tab
- See all workflow runs and their status

### View Test Results
- Click on any workflow run
- Go to the **Test** job
- Click "Test Results" in the summary
- See detailed test report with pass/fail status

### View Deployment
- After successful deployment, Vercel will comment on commits with deployment URL
- Production URL: `https://combo-coach.vercel.app` (or your custom domain)

## Troubleshooting

### Tests Fail in CI but Pass Locally
```bash
# Clean and test exactly like CI does
./gradlew clean jsNodeTest
```

### Build Fails in CI
```bash
# Test production build locally
./gradlew jsBrowserProductionWebpack
# Check output in BUILD_OUTPUT_DIR and RESOURCES_DIR specified in ./github/workflows/cicd.yml
```

### Deployment Fails
1. Verify all three Vercel secrets are set correctly
2. Check Vercel dashboard for deployment logs

### Pipeline Not Triggering
- Check if branch name matches workflow triggers
- Ensure `.github/workflows/cicd.yml` is pushed to GitHub
- Check Actions tab for any disabled workflows