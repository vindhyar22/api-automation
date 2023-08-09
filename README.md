# lock-api-automation
add a project to a Git repository hosted on a URL
cd C:\Projects\my_project
git init
git add .
git commit -m "Initial commit"
git remote add origin <remote_url>
git push -u origin master

changes have been made to the remote repository that you haven't pulled into your local repository yet.
git pull origin master
git commit -am "Your commit message"
git push origin master


replace the existing code in a remote branch with your new project
git clone https://github.com/vindhyar22/lock-api-automation.git
cd lock-api-automation
git rm -r *
git add .
git commit -m "Added new project"
git push -f origin master
