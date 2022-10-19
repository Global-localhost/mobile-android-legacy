package uk.openvk.android.legacy.api;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import uk.openvk.android.legacy.api.models.Friend;
import uk.openvk.android.legacy.api.models.Photo;
import uk.openvk.android.legacy.api.wrappers.DownloadManager;
import uk.openvk.android.legacy.api.wrappers.JSONParser;
import uk.openvk.android.legacy.api.wrappers.OvkAPIWrapper;

public class Friends implements Parcelable {
    private JSONParser jsonParser;
    private ArrayList<Friend> friends;
    private DownloadManager downloadManager;

    public Friends() {
        jsonParser = new JSONParser();
        friends = new ArrayList<Friend>();
    }

    public Friends(String response, DownloadManager downloadManager, boolean downloadPhoto) {
        jsonParser = new JSONParser();
        parse(response, downloadManager, downloadPhoto);
    }

    protected Friends(Parcel in) {
    }

    public static final Creator<Friends> CREATOR = new Creator<Friends>() {
        @Override
        public Friends createFromParcel(Parcel in) {
            return new Friends(in);
        }

        @Override
        public Friends[] newArray(int size) {
            return new Friends[size];
        }
    };

    public void parse(String response, DownloadManager downloadManager, boolean downloadPhoto) {
        try {
            this.friends.clear();
            JSONObject json = jsonParser.parseJSON(response).getJSONObject("response");
            JSONArray users = json.getJSONArray("items");
            ArrayList<Photo> avatars;
            avatars = new ArrayList<Photo>();
            for (int i = 0; i < users.length(); i++) {
                Friend friend = new Friend(users.getJSONObject(i));
                Photo photo = new Photo();
                photo.url = friend.avatar_url;
                photo.filename = String.format("avatar_%d", friend.id);
                avatars.add(photo);
                this.friends.add(friend);
            }
            if(downloadPhoto) {
                downloadManager.downloadPhotosToCache(avatars, "friend_avatars");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void get(OvkAPIWrapper ovk, int user_id, String where) {
        ovk.sendAPIMethod("Friends.get", String.format("user_id=%d&fields=verified,online,photo_100", user_id), where);
    }

    public ArrayList<Friend> getFriends() {
        return friends;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }

    public void add(OvkAPIWrapper ovk, int user_id) {
        ovk.sendAPIMethod("Friends.add", String.format("user_id=%d", user_id));
    }

    public void delete(OvkAPIWrapper ovk, int user_id) {
        ovk.sendAPIMethod("Friends.delete", String.format("user_id=%d", user_id));
    }
}
