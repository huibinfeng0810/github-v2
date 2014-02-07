/*
 * Copyright 2012 GitHub Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.mobile;

import com.github.mobile.core.search.SearchUserService;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.*;

import java.io.IOException;

/**
 * Provide GitHub-API related services
 */
public class ServicesModule extends AbstractModule {

    @Override
    protected void configure() {
    }

    @Provides
    IssueService issueService(GitHubClient client) {
        return new IssueService(client);
    }

    @Provides
    PullRequestService pullRequestService(GitHubClient client) {
        return new PullRequestService(client);
    }

    @Provides
    UserService userService(GitHubClient client) {
        return new UserService(client);
    }

    @Provides
    SearchUserService searchUserService(GitHubClient client) {
        return new SearchUserService(client);
    }

    @Provides
    GistService gistService(GitHubClient client) {
        return new GistService(client);
    }

    @Provides
    OrganizationService orgService(GitHubClient client) {
        return new OrganizationService(client);
    }

    @Provides
    RepositoryService repoService(GitHubClient client) {
        return new RepositoryService(client);
    }

    @Provides
    User currentUser(UserService userService) throws IOException {
        return userService.getUser();
    }

    @Provides
    CollaboratorService collaboratorService(GitHubClient client) {
        return new CollaboratorService(client);
    }

    @Provides
    MilestoneService milestoneService(GitHubClient client) {
        return new MilestoneService(client);
    }

    @Provides
    LabelService labelService(GitHubClient client) {
        return new LabelService(client);
    }

    @Provides
    EventService eventService(GitHubClient client) {
        return new EventService(client);
    }

    @Provides
    WatcherService watcherService(GitHubClient client) {
        return new WatcherService(client);
    }

    @Provides
    CommitService commitService(GitHubClient client) {
        return new CommitService(client);
    }

    @Provides
    DataService dataService(GitHubClient client) {
        return new DataService(client);
    }

    @Provides
    MarkdownService markdownService(GitHubClient client) {
        return new MarkdownService(client);
    }

    @Provides
    ContentsService contentsService(GitHubClient client) {
        return new ContentsService(client);
    }
}
