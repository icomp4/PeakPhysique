# Developer tools
## Setup Steps
1. Install: https://cli.github.com/
2. Git Commit Template Setup: git config commit.template commit-template.txt
## Create a Pull Request
1. Create a development branch: git branch <DEV_BRANCH>
2. Checkout development branch: git checkout <DEV_BRANCH>
3. Create commit using the template: git commit
4. Create a Pull Request: gh pr create -a "@me" --base "main"

