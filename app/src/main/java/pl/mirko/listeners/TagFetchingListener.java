package pl.mirko.listeners;

import java.util.List;

public interface TagFetchingListener {

    void onTagFetchingFinished(List<String> tags);
}
