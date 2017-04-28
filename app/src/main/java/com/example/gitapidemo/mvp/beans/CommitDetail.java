package com.example.gitapidemo.mvp.beans;

/**
 * Created by mithilesh on 4/24/17.
 */
public class CommitDetail {

    private String commiterName;
    private String commitMessage;
    private String commiterAvatar;

    public String getCommiterName() {
        return commiterName;
    }

    public void setCommiterName(String commiterName) {
        this.commiterName = commiterName;
    }

    public String getCommitMessage() {
        return commitMessage;
    }

    public void setCommitMessage(String commitMessage) {
        this.commitMessage = commitMessage;
    }

    public String getCommiterAvatar() {
        return commiterAvatar;
    }

    public void setCommiterAvatar(String commiterAvatar) {
        this.commiterAvatar = commiterAvatar;
    }

    @Override
    public String toString() {
        return "CommitDetail{" +
                "commiterName='" + commiterName + '\'' +
                ", commitMessage='" + commitMessage + '\'' +
                ", commiterAvatar='" + commiterAvatar + '\'' +
                '}';
    }
}
