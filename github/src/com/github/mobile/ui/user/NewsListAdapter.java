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
package com.github.mobile.ui.user;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.github.kevinsawicki.wishlist.ViewUtils;
import com.github.mobile.R.id;
import com.github.mobile.core.issue.IssueUtils;
import com.github.mobile.ui.StyledText;
import com.github.mobile.util.AvatarLoader;
import com.github.mobile.util.TimeUtils;
import com.github.mobile.util.TypefaceUtils;
import org.eclipse.egit.github.core.*;
import org.eclipse.egit.github.core.event.*;

import java.util.List;

import static com.github.kevinsawicki.wishlist.ViewUpdater.FORMAT_INT;
import static com.github.mobile.util.TypefaceUtils.*;
import static org.eclipse.egit.github.core.event.Event.*;

/**
 * Adapter for a list of news events
 */
public class NewsListAdapter extends SingleTypeAdapter<Event> {

    /**
     * Can the given event be rendered by this view holder?
     *
     * @param event
     * @return true if renderable, false otherwise
     */
    public static boolean isValid(final Event event) {
        if (event == null)
            return false;

        final EventPayload payload = event.getPayload();
        if (payload == null || EventPayload.class.equals(payload.getClass()))
            return false;

        final String type = event.getType();
        if (TextUtils.isEmpty(type))
            return false;

        return TYPE_COMMIT_COMMENT.equals(type) //
                || (TYPE_CREATE.equals(type) //
                && ((CreatePayload) payload).getRefType() != null) //
                || TYPE_DELETE.equals(type) //
                || TYPE_DOWNLOAD.equals(type) //
                || TYPE_FOLLOW.equals(type) //
                || TYPE_FORK.equals(type) //
                || TYPE_FORK_APPLY.equals(type) //
                || (TYPE_GIST.equals(type) //
                && ((GistPayload) payload).getGist() != null) //
                || TYPE_GOLLUM.equals(type) //
                || (TYPE_ISSUE_COMMENT.equals(type) //
                && ((IssueCommentPayload) payload).getIssue() != null) //
                || (TYPE_ISSUES.equals(type) //
                && ((IssuesPayload) payload).getIssue() != null) //
                || TYPE_MEMBER.equals(type) //
                || TYPE_PUBLIC.equals(type) //
                || TYPE_PULL_REQUEST.equals(type) //
                || TYPE_PULL_REQUEST_REVIEW_COMMENT.equals(type) //
                || TYPE_PUSH.equals(type) //
                || TYPE_TEAM_ADD.equals(type) //
                || TYPE_WATCH.equals(type);
    }

    private static void appendComment(final StyledText details,
            final Comment comment) {
        if (comment != null)
            appendText(details, comment.getBody());
    }

    private static void appendCommitComment(final StyledText details,
            final CommitComment comment) {
        if (comment == null)
            return;

        String id = comment.getCommitId();
        if (!TextUtils.isEmpty(id)) {
            if (id.length() > 10)
                id = id.substring(0, 10);
            appendText(details, "Comment in");
            details.append(' ');
            details.monospace(id);
            details.append(':').append('\n');
        }
        appendComment(details, comment);
    }

    private static void appendText(final StyledText details, String text) {
        if (text == null)
            return;
        text = text.trim();
        if (text.length() == 0)
            return;

        details.append(text);
    }

    private static StyledText boldActor(final StyledText text, final Event event) {
        return boldUser(text, event.getActor());
    }

    private static StyledText boldUser(final StyledText text, final User user) {
        if (user != null)
            text.bold(user.getLogin());
        return text;
    }

    private static StyledText boldRepo(final StyledText text, final Event event) {
        EventRepository repo = event.getRepo();
        if (repo != null)
            text.bold(repo.getName());
        return text;
    }

    private static StyledText boldRepoName(final StyledText text,
            final Event event) {
        EventRepository repo = event.getRepo();
        if (repo != null) {
            String name = repo.getName();
            if (!TextUtils.isEmpty(name)) {
                int slash = name.indexOf('/');
                if (slash != -1 && slash + 1 < name.length())
                    text.bold(name.substring(slash + 1));
            }
        }
        return text;
    }

    private static void formatCommitComment(Event event, StyledText main,
            StyledText details) {
        boldActor(main, event);
        main.append(" commented on ");
        boldRepo(main, event);

        CommitCommentPayload payload = (CommitCommentPayload) event
                .getPayload();
        appendCommitComment(details, payload.getComment());
    }

    private static void formatDownload(Event event, StyledText main,
            StyledText details) {
        boldActor(main, event);
        main.append(" uploaded a file to ");
        boldRepo(main, event);

        DownloadPayload payload = (DownloadPayload) event.getPayload();
        Download download = payload.getDownload();
        if (download != null)
            appendText(details, download.getName());
    }

    private static void formatCreate(Event event, StyledText main,
            StyledText details) {
        boldActor(main, event);

        main.append(" created ");
        CreatePayload payload = (CreatePayload) event.getPayload();
        String refType = payload.getRefType();
        main.append(refType);
        main.append(' ');
        if (!"repository".equals(refType)) {
            main.append(payload.getRef());
            main.append(" at ");
            boldRepo(main, event);
        } else
            boldRepoName(main, event);
    }

    private static void formatDelete(Event event, StyledText main,
            StyledText details) {
        boldActor(main, event);

        DeletePayload payload = (DeletePayload) event.getPayload();
        main.append(" deleted ");
        main.append(payload.getRefType());
        main.append(' ');
        main.append(payload.getRef());
        main.append(" at ");

        boldRepo(main, event);
    }

    private static void formatFollow(Event event, StyledText main,
            StyledText details) {
        boldActor(main, event);
        main.append(" started following ");
        boldUser(main, ((FollowPayload) event.getPayload()).getTarget());
    }

    private static void formatFork(Event event, StyledText main,
            StyledText details) {
        boldActor(main, event);
        main.append(" forked repository ");
        boldRepo(main, event);
    }

    private static void formatGist(Event event, StyledText main,
            StyledText details) {
        boldActor(main, event);

        GistPayload payload = (GistPayload) event.getPayload();

        main.append(' ');
        String action = payload.getAction();
        if ("create".equals(action))
            main.append("created");
        else if ("update".equals(action))
            main.append("updated");
        else
            main.append(action);
        main.append(" Gist ");
        main.append(payload.getGist().getId());
    }

    private static void formatWiki(Event event, StyledText main,
            StyledText details) {
        boldActor(main, event);
        main.append(" updated the wiki in ");
        boldRepo(main, event);
    }

    private static void formatIssueComment(Event event, StyledText main,
            StyledText details) {
        boldActor(main, event);

        main.append(" commented on ");

        IssueCommentPayload payload = (IssueCommentPayload) event.getPayload();

        Issue issue = payload.getIssue();
        String number;
        if (IssueUtils.isPullRequest(issue))
            number = "pull request " + issue.getNumber();
        else
            number = "issue " + issue.getNumber();
        main.bold(number);

        main.append(" on ");

        boldRepo(main, event);

        appendComment(details, payload.getComment());
    }

    private static void formatIssues(Event event, StyledText main,
            StyledText details) {
        boldActor(main, event);

        IssuesPayload payload = (IssuesPayload) event.getPayload();
        String action = payload.getAction();
        Issue issue = payload.getIssue();
        main.append(' ');
        main.append(action);
        main.append(' ');
        main.bold("issue " + issue.getNumber());
        main.append(" on ");

        boldRepo(main, event);

        appendText(details, issue.getTitle());
    }

    private static void formatAddMember(Event event, StyledText main,
            StyledText details) {
        boldActor(main, event);
        main.append(" added ");
        User member = ((MemberPayload) event.getPayload()).getMember();
        if (member != null)
            main.bold(member.getLogin());
        main.append(" as a collaborator to ");
        boldRepo(main, event);
    }

    private static void formatPublic(Event event, StyledText main,
            StyledText details) {
        boldActor(main, event);
        main.append(" open sourced repository ");
        boldRepo(main, event);
    }

    private static void formatWatch(Event event, StyledText main,
            StyledText details) {
        boldActor(main, event);
        main.append(" starred ");
        boldRepo(main, event);
    }

    private static void formatReviewComment(Event event, StyledText main,
            StyledText details) {
        boldActor(main, event);
        main.append(" commented on ");
        boldRepo(main, event);

        PullRequestReviewCommentPayload payload = (PullRequestReviewCommentPayload) event
                .getPayload();
        appendCommitComment(details, payload.getComment());
    }

    private static void formatPullRequest(Event event, StyledText main,
            StyledText details) {
        boldActor(main, event);

        PullRequestPayload payload = (PullRequestPayload) event.getPayload();
        String action = payload.getAction();
        if ("synchronize".equals(action))
            action = "updated";
        main.append(' ');
        main.append(action);
        main.append(' ');
        main.bold("pull request " + payload.getNumber());
        main.append(" on ");

        boldRepo(main, event);

        if ("opened".equals(action) || "closed".equals(action)) {
            PullRequest request = payload.getPullRequest();
            if (request != null) {
                String title = request.getTitle();
                if (!TextUtils.isEmpty(title))
                    details.append(title);
            }
        }
    }

    private static void formatPush(Event event, StyledText main,
            StyledText details) {
        boldActor(main, event);

        main.append(" pushed to ");
        PushPayload payload = (PushPayload) event.getPayload();
        String ref = payload.getRef();
        if (ref.startsWith("refs/heads/"))
            ref = ref.substring(11);
        main.bold(ref);
        main.append(" at ");

        boldRepo(main, event);

        final List<Commit> commits = payload.getCommits();
        int size = commits != null ? commits.size() : -1;
        if (size > 0) {
            if (size != 1)
                details.append(FORMAT_INT.format(size)).append(" new commits");
            else
                details.append("1 new commit");

            int max = 3;
            int appended = 0;
            for (Commit commit : commits) {
                if (commit == null)
                    continue;

                String sha = commit.getSha();
                if (TextUtils.isEmpty(sha))
                    continue;

                details.append('\n');
                if (sha.length() > 7)
                    details.monospace(sha.substring(0, 7));
                else
                    details.monospace(sha);

                String message = commit.getMessage();
                if (!TextUtils.isEmpty(message)) {
                    details.append(' ');
                    int newline = message.indexOf('\n');
                    if (newline > 0)
                        details.append(message.subSequence(0, newline));
                    else
                        details.append(message);
                }

                appended++;
                if (appended == max)
                    break;
            }
        }
    }

    private static void formatTeamAdd(Event event, StyledText main,
            StyledText details) {
        boldActor(main, event);

        TeamAddPayload payload = (TeamAddPayload) event.getPayload();

        main.append(" added ");

        User user = payload.getUser();
        if (user != null)
            boldUser(main, user);
        else
            boldRepoName(main, event);

        main.append(" to team");

        Team team = payload.getTeam();
        String teamName = team != null ? team.getName() : null;
        if (teamName != null)
            main.append(' ').bold(teamName);
    }

    private final AvatarLoader avatars;

    /**
     * Create list adapter
     *
     * @param inflater
     * @param elements
     * @param avatars
     */
    public NewsListAdapter(LayoutInflater inflater, Event[] elements,
            AvatarLoader avatars) {
        super(inflater, com.github.mobile.R.layout.news_item);

        this.avatars = avatars;
        setItems(elements);
    }

    /**
     * Create list adapter
     *
     *
     * @param inflater
     * @param avatars
     */
    public NewsListAdapter(LayoutInflater inflater, AvatarLoader avatars) {
        this(inflater, null, avatars);
    }

    @Override
    public long getItemId(final int position) {
        final String id = getItem(position).getId();
        return !TextUtils.isEmpty(id) ? id.hashCode() : super
                .getItemId(position);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[] { id.iv_avatar, id.tv_event, id.tv_event_details,
                id.tv_event_icon, id.tv_event_date };
    }

    @Override
    protected View initialize(View view) {
        view = super.initialize(view);

        TypefaceUtils.setOcticons(textView(view, 3));
        return view;
    }

    @Override
    protected void update(int position, Event event) {
        avatars.bind(imageView(0), event.getActor());

        StyledText main = new StyledText();
        StyledText details = new StyledText();
        String icon = null;

        String type = event.getType();
        if (TYPE_COMMIT_COMMENT.equals(type)) {
            icon = ICON_COMMENT;
            formatCommitComment(event, main, details);
        } else if (TYPE_CREATE.equals(type)) {
            icon = ICON_CREATE;
            formatCreate(event, main, details);
        } else if (TYPE_DELETE.equals(type)) {
            icon = ICON_DELETE;
            formatDelete(event, main, details);
        } else if (TYPE_DOWNLOAD.equals(type)) {
            icon = ICON_UPLOAD;
            formatDownload(event, main, details);
        } else if (TYPE_FOLLOW.equals(type)) {
            icon = ICON_FOLLOW;
            formatFollow(event, main, details);
        } else if (TYPE_FORK.equals(type)) {
            icon = ICON_FORK;
            formatFork(event, main, details);
        } else if (TYPE_GIST.equals(type)) {
            icon = ICON_GIST;
            formatGist(event, main, details);
        } else if (TYPE_GOLLUM.equals(type)) {
            icon = ICON_WIKI;
            formatWiki(event, main, details);
        } else if (TYPE_ISSUE_COMMENT.equals(type)) {
            icon = ICON_ISSUE_COMMENT;
            formatIssueComment(event, main, details);
        } else if (TYPE_ISSUES.equals(type)) {
            String action = ((IssuesPayload) event.getPayload()).getAction();
            if ("opened".equals(action))
                icon = ICON_ISSUE_OPEN;
            else if ("reopened".equals(action))
                icon = ICON_ISSUE_REOPEN;
            else if ("closed".equals(action))
                icon = ICON_ISSUE_CLOSE;
            formatIssues(event, main, details);
        } else if (TYPE_MEMBER.equals(type)) {
            icon = ICON_ADD_MEMBER;
            formatAddMember(event, main, details);
        } else if (TYPE_PUBLIC.equals(type))
            formatPublic(event, main, details);
        else if (TYPE_PULL_REQUEST.equals(type)) {
            icon = ICON_PULL_REQUEST;
            formatPullRequest(event, main, details);
        } else if (TYPE_PULL_REQUEST_REVIEW_COMMENT.equals(type)) {
            icon = ICON_COMMENT;
            formatReviewComment(event, main, details);
        } else if (TYPE_PUSH.equals(type)) {
            icon = ICON_PUSH;
            formatPush(event, main, details);
        } else if (TYPE_TEAM_ADD.equals(type)) {
            icon = ICON_ADD_MEMBER;
            formatTeamAdd(event, main, details);
        } else if (TYPE_WATCH.equals(type)) {
            icon = ICON_STAR;
            formatWatch(event, main, details);
        }

        if (icon != null)
            ViewUtils.setGone(setText(3, icon), false);
        else
            setGone(3, true);

        setText(1, main);

        if (!TextUtils.isEmpty(details))
            ViewUtils.setGone(setText(2, details), false);
        else
            setGone(2, true);

        setText(4, TimeUtils.getRelativeTime(event.getCreatedAt()));
    }
}