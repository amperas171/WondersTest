package com.amperas17.wonderstest.data.repository;


import com.amperas17.wonderstest.model.pojo.Issue;
import com.amperas17.wonderstest.model.realm.RealmIssue;

import java.util.ArrayList;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;

public class IssuesRepository {

    private Realm realm;

    public IssuesRepository() {
        realm = Realm.getDefaultInstance();
    }

    public RealmResults<RealmIssue> getIssuesByRepoName(final String repoName) {
        return realm.where(RealmIssue.class)
                .equalTo(RealmIssue.REPO_NAME, repoName)
                .findAll();
    }

    public RealmResults<RealmIssue> getAllIssues() {
        return realm.where(RealmIssue.class).findAll();
    }

    public RealmResults<RealmIssue> getSearchedIssues(String pattern) {
        return realm.where(RealmIssue.class)
                .contains(RealmIssue.TITLE, pattern, Case.INSENSITIVE)
                .findAllSorted(RealmIssue.TITLE);
    }

    public void setIssues(final ArrayList<Issue> issues, final String repoName) {
        if (!realm.isClosed())
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    for (Issue issue : issues) {
                        RealmIssue realmIssue = new RealmIssue(issue);
                        realmIssue.setRepoName(repoName);
                        realm.insertOrUpdate(realmIssue);
                    }
                }
            });
    }

    public void close() {
        if (realm != null) {
            realm.close();
        }
    }
}