package com.warpcast.analysis.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

public class Channel {

    /**
     * Data model for Warpcast channels
     */
    private String id;
    private String url;
    private String name;
    private String description;
    private String imageUrl;
    private String leadFid;
    private String moderatorFid;
    private List<String> hostFids;
    private long createdAt;
    private int followerCount;

    public Channel() {
    }

    @JsonCreator
    public Channel(
            @JsonProperty("id") String id,
            @JsonProperty("url") String url,
            @JsonProperty("name") String name,
            @JsonProperty("description") String description,
            @JsonProperty("imageUrl") String imageUrl,
            @JsonProperty("leadFid") String leadFid,
            @JsonProperty("moderatorFid") String moderatorFid,
            @JsonProperty("hostFids") List<String> hostFids,
            @JsonProperty("createdAt") long createdAt,
            @JsonProperty("followerCount") int followerCount
    ) {
        this.id = id;
        this.url = url;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.leadFid = leadFid;
        this.moderatorFid = moderatorFid;
        this.hostFids = hostFids;
        this.createdAt = createdAt;
        this.followerCount = followerCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLeadFid() {
        return leadFid;
    }

    public void setLeadFid(String leadFid) {
        this.leadFid = leadFid;
    }

    public String getModeratorFid() {
        return moderatorFid;
    }

    public void setModeratorFid(String moderatorFid) {
        this.moderatorFid = moderatorFid;
    }

    public List<String> getHostFids() {
        return hostFids;
    }

    public void setHostFids(List<String> hostFids) {
        this.hostFids = hostFids;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Channel channel = (Channel) o;
        return getCreatedAt() == channel.getCreatedAt() && getFollowerCount() == channel.getFollowerCount() && Objects.equals(getId(), channel.getId()) && Objects.equals(getUrl(), channel.getUrl()) && Objects.equals(getName(), channel.getName()) && Objects.equals(getDescription(), channel.getDescription()) && Objects.equals(getImageUrl(), channel.getImageUrl()) && Objects.equals(getLeadFid(), channel.getLeadFid()) && Objects.equals(getHostFids(), channel.getHostFids());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUrl(), getName(), getDescription(), getImageUrl(), getLeadFid(), getHostFids(), getCreatedAt(), getFollowerCount());
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", leadFid='" + leadFid + '\'' +
                ", hostFids=" + hostFids +
                ", createdAt=" + createdAt +
                ", followerCount=" + followerCount +
                '}';
    }
}
