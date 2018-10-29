package ch.epfl.sweng.swenggolf.storage;

import android.net.Uri;

import java.util.Map;
import java.util.TreeMap;

import ch.epfl.sweng.swenggolf.offer.Offer;

public final class FakeStorage extends Storage {

    private final Map<String, Object> storage;
    private final boolean working;

    public FakeStorage(boolean working) {
        this.storage = new TreeMap<>();
        this.working = working;
    }

    @Override
    public void write(Uri uri, Offer offer) {

    }

    @Override
    public void remove(String linkPicture) {

    }

}
