# Guide to contributing

Please read this if you intend to contribute to the project.

## Developer Guidelines

The full set of ICE developer guidelines are availalbe [here](https://wiki.eclipse.org/ICE_Developer_Documentation). You should read them before proceeding.

## Legal stuff

Apologies in advance for the extra work required here - this is necessary to comply with the Eclipse Foundation's
strict IP policy.

Please also read [this](http://wiki.eclipse.org/Development_Resources/Contributing_via_Git).

In order for any contributions to be accepted, you MUST do the following things:

* Sign the [Eclipse Contributor Agreement](http://www.eclipse.org/legal/ECA.php).
To sign the Eclipse Contributor Agreement you need to:

  * Obtain an Eclipse Foundation account. Anyone who currently uses Eclipse Bugzilla or Gerrit already has an Eclipse account.
If you don’t, you need to [register](https://dev.eclipse.org/site_login/createaccount.php).

  * Login to the [projects portal](https://projects.eclipse.org/), select “My Account”, and then the “Eclipse ECA” tab.

* Add your GitHub ID in your Eclipse Foundation account. Find this option in the "Account Settings" tab.

* "Sign-off" your commits

Every commit you make in your patch or pull request MUST be "signed off".

You do this by adding the `-s` flag when you make the commit(s), e.g.

    git commit -s -m "Shave the yak some more"

## Making your changes

If you are not a committer on this project, please follow these steps to submit your changes:
* Fork the repository on GitHub.
* Create a new branch for your changes.
* Make your changes.
* Make sure you include tests.
* Make sure the test suite passes after your changes.
* Commit your changes into that branch.
* Use descriptive and meaningful commit messages.
* If you have a lot of commits, squash them into a single commit.
* Make sure you use the `-s` flag when committing as explained above.
* Push your changes to your branch in your forked repository and create a Pull Request.

## Branch Policy

ICE features are developed with a one branch per feature policy. Do not use your branch for any other feature development. This prevents crossing the development streams for different features and accelerates the PR review process.

Branches should be named in the style of <your name>/<feature name>. This scheme makes it easy to quickly identify who is working on a branch and the feature under development. A branch named jay/tasks is correctly named, but a branch named tasks-202010 is not. 
 
Branches should be merged regularly against their parent. ```git merge``` is the preferred merging mechanism to preserve as much history as possible. ```git rebase``` may also be used, but preferrably only for those cases where a rebase is required to fix problems and not solely to squash the history.

## Code quality requirements

All submissions are reviewed for technical merit. In addition to a technical review, pull requests are reviewed for code quality to maintain a standard across the code base. This includes:
* Reviewing tests to understand the client-facing API of the submission and its general functionality.
* Insuring sufficient test coverage such that at least 80% (preferrably more) of the submission is executed by the tests.
* Insuring that the code satisfies basic quality metrics in a code quality tool (linter).
* Reviewing the general look and feel, including the format, of the submission.

Generated classes, including those from annotation processors, Lombok, and other tools, may not need to have unit tests.

The project committers reserve the right to ask for more tests and other improvements to quality for any submission.

## Submitting the changes

Submit a pull request via the normal GitHub UI.

# Credit

This document was originally written by the Vert.x team at

https://raw.githubusercontent.com/eclipse/vert.x/master/CONTRIBUTING.md

We have shamelessly copied, modified and co-opted it for our own repo and we
graciously acknowledge the work of the original authors.
