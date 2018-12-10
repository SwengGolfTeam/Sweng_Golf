package ch.epfl.sweng.swenggolf.database;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.swenggolf.offer.Category;
import ch.epfl.sweng.swenggolf.offer.Offer;
import ch.epfl.sweng.swenggolf.profile.User;

/**
 * FakeDatabase filled for tests purposes.
 */
public final class FilledFakeDatabase extends FakeDatabase {

    public static final double FAKE_LATITUDE = 44.34;
    public static final double FAKE_LONGITUDE = 1.21;
    private static final String WIKIA_NO_COOKIE = "https://vignette.wikia.nocookie.net/";
    private static final String LUMIERE = "https://lumiere-a.akamaihd.net/v1/images/";
    private static final String STARWARS = "starwars/";
    private static final long DATE = 123123123L;
    private static final User[] FAKE_USERS = {
            new User("C3PO", "0", "c3po@gmail.com",
                    WIKIA_NO_COOKIE + STARWARS
                            + "images/5/51/C-3PO_EP3.png/revision/"
                            + "latest?cb=20131005124036",
                    "Peace"),
            new User("Anakin", "1", "childrenkiller@gmail.com",
                    "https://upload.wikimedia.org/"
                            + "wikipedia/en/thumb/7/76/"
                            + "Darth_Vader.jpg/220px-Darth_Vader.jpg",
                    "The high ground"),
            new User("Leia", "2", "brotherlove@hotmail.com",
                    "https://www.lepoint.fr/"
                            + "images/2016/12/27/"
                            + "6576537lpw-6576611-article-jpg_3988009.jpg",
                    "Resistance to the empire"),
            new User("Luke", "3", "armless@yahoo.com",
                    WIKIA_NO_COOKIE
                            + "fr.starwars/"
                            + "images/2/2d/Luke.jpg/revision/"
                            + "latest?cb=20150618122007",
                    "A new arm"),
            new User("Palapatine", "4", "doit@gmail.com",
                    "https://pbs.twimg.com/profile_images/"
                            + "575699272303140864/tuvCN6-E.jpeg",
                    "The senate"),
            new User("Mace Windu", "5", "notyet@hotmail.com",
                    "https://upload.wikimedia.org/wikipedia/en/thumb/b"
                            + "/bf/Mace_Windu.png/220px-Mace_Windu.png",
                    "A seat for the young Skywalker"),
            new User("Jar Jar", "6", "sithmaster@outlook.com",
                    "https://cdn3.movieweb.com/i/article/"
                            + "xgrbrgHUykxiAXgtYjBo5JVrs4b99J/798:50/"
                            + "Star-Wars-Darth-Jar-Jar-Sith-Fan-Theory.jpg",
                    "An apprentice"),
            new User("Yoda", "7", "greenone@mail.com",
                    "https://upload.wikimedia.org/wikipedia/en/thumb/6/6f/"
                            + "Yoda_Attack_of_the_Clones.png/"
                            + "170px-Yoda_Attack_of_the_Clones.png",
                    "A decent prequel"),
            new User("Obi Wan", "8", "boldone@gmail.com",
                    "https://static.fnac-static.com/multimedia/Images/FD/"
                            + "Comete/93262/CCP_IMG_ORIGINAL/1188071.jpg",
                    "Balance to the force"),
            new User("R2D2", "9", "bip@gmail.com",
                    "https://boutique.orange.fr/media-cms/mediatheque/"
                            + "504x504-r2d2-vue-2-101962.jpg",
                    "biupbiptut"),
            new User("Qui-Gon Jinn", "10", "liam@hotmail.com",
                    "https://www.starwars-universe.com/images/encyclopedie/"
                            + "personnages/images_v6/quigon_imv6.jpg",
                    "My daughter"),
            new User("BB8", "11", "bop@yahoo.com",
                    "https://gloimg.gbtcdn.com/gb/pdm-provider-img/"
                            + "straight-product-img/20171129/T014099/T0140990154/"
                            + "goods-img/1511931948337193297.jpg",
                    "tittutbipmip"),
            new User("Padme", "13", "princess@outlook.com",
                    "https://encrypted-tbn0.gstatic.com/"
                            + "images?q=tbn:ANd9GcT9I_hNNXaHQ31i6s-_EsOeUv8tMRWPe7C"
                            + "_mVgZLrAQM3cGzxeU",
                    "Anakin"),
            new User("Poe", "12", "xwingdriver@gmail.com",
                    WIKIA_NO_COOKIE
                            + "fr.starwars/images/1/1d/"
                            + "Poe_Dameron.png/revision/latest?cb=20161110202556",
                    "Spaceships"),
            new User("Dark Maul", "14", "halfman@gmail.com",
                    WIKIA_NO_COOKIE + STARWARS
                            + "images/7/79/Maul_SASWS_Forbes_Promo_HS.png/"
                            + "revision/latest?cb=20180909043811",
                    "Face make up"),
            new User("Jabba The Hutt", "15", "slut@yahoo.com",
                    WIKIA_NO_COOKIE
                            + "fr.starwars/images/3/39/"
                            + "Jabba_le_Hutt.png/revision/latest?cb=20170818180549",
                    "Carbonite Han Solo"),
            new User("Dark Plagueis", "16", "thewise@hotmail.com",
                    "",
                    "Save myself from death"),
            new User("Grievous", "17", "hellothere@yahoo.com",
                    "https://lumiere-a.akamaihd.net/v1/"
                            + "images/General-Grievous_c9df9cb5.jpeg?"
                            + "region=0%2C0%2C1200%2C675&width=768",
                    "Lightsabers")
    };
    private static final Offer[] FAKE_OFFERS = {
            new Offer.Builder().setUserId("0")
                    .setTitle("Little Droid for ride")
                    .setDescription("I have a little droid with informations"
                            + " on the rebels care to exchange it against"
                            + " a ride from Tatooine ?")
                    .setLinkPicture(WIKIA_NO_COOKIE + STARWARS
                            + "images/f/ff/Sandcrawler.png/"
                            + "revision/latest?cb=20130812001443")
                    .setUuid("01").setTag(Category.values()[0])
                    .setCreationDate(DATE).setEndDate(DATE).setLocation(getLocation()).build(),

            new Offer.Builder().setUserId("13")
                    .setTitle("Defense against the droids")
                    .setDescription("The Trade Federation is attacking my planet,"
                            + " I need help ! I have some nice clothes"
                            + " I can exchange !")
                    .setLinkPicture(LUMIERE
                            + "databank_battledroid_01_169_1524f145.jpeg?"
                            + "region=0%2C0%2C1560%2C878&width=768")
                    .setUuid("02").setTag(Category.values()[1])
                    .setCreationDate(DATE).setEndDate(DATE).setLocation(getLocation()).build(),

            new Offer.Builder().setUserId("8")
                    .setTitle("Chosen one")
                    .setDescription("Someone out there is the chosen one ?"
                            + " If you are I can train you !"
                            + " Warning, last apprentice got bad burns !")
                    .setUuid("03").setTag(Category.values()[1])
                    .setCreationDate(DATE).setEndDate(DATE).build(),

            new Offer.Builder().setUserId("15")
                    .setTitle("Great Price to find a friend")
                    .setDescription("I'm looking for a \"friend\" of mine, a certain Han Solo,"
                            + " I offer a desert spaceship to interested !"
                            + " It's him on the left.")
                    .setLinkPicture("https://cdn3.whatculture.com/"
                            + "images/2014/12/Star-Wars-Special-Edition-Jabba-600x400.jpg")
                    .setUuid("04").setTag(Category.values()[2])
                    .setCreationDate(DATE).setEndDate(DATE).build(),

            new Offer.Builder().setUserId("7")
                    .setTitle("Defeat Dark Sidious I must")
                    .setDescription("Defeat Dark Sidious. "
                            + "With me train you shall,"
                            + " If so you want.")
                    .setLinkPicture(WIKIA_NO_COOKIE + STARWARS
                            + "images/2/23/Gngf.jpg/revision/latest?cb=20080326171911")
                    .setUuid("05").setTag(Category.values()[2])
                    .setCreationDate(DATE).setEndDate(DATE).build(),

            new Offer.Builder().setUserId("4")
                    .setTitle("Help to get the senate")
                    .setDescription("I'm looking for an apprentice to show him"
                            + " my unlimited power and take down the senate !")
                    .setLinkPicture(LUMIERE
                            + "galactic-senate-3_9351812c.jpeg?region=0%2C0%2C800%2C342")
                    .setUuid("06").setTag(Category.values()[1])
                    .setCreationDate(DATE).setEndDate(DATE).build(),

            new Offer.Builder().setUserId("5")
                    .setTitle("Prepare Surprise for a friend")
                    .setDescription("Someone would like to help me prepare a surprise "
                            + "for a friend ? Create a display with \"NOT YET !\""
                            + " on it. I'll invite you to a beer then.")
                    .setUuid("07").setTag(Category.values()[2])
                    .setCreationDate(DATE).setEndDate(DATE).build(),

            new Offer.Builder().setUserId("10")
                    .setTitle("Take revenge on my apprentice")
                    .setDescription("Need someone to find my apprentice, some \"bat\" guy. "
                            + "I'll show you the League of Shadows !")
                    .setUuid("08").build(),

            new Offer.Builder().setUserId("9")
                    .setTitle("bipbupbap")
                    .setDescription("titut bip bop tilit tut tut tat dut dut ! Mip zat zat !")
                    .setLinkPicture(LUMIERE
                            + "jawas_42e63e07.jpeg?region=866%2C10%2C1068%2C601&width=768")
                    .setTag(Category.values()[3])
                    .setCreationDate(DATE)
                    .setEndDate(DATE)
                    .setUuid("09").build(),

            new Offer.Builder().setUserId("1")
                    .setTitle("Nice helmet to build ship")
                    .setDescription("I must build some star like ship,"
                            + " you can have my helmet then !")
                    .setLinkPicture(LUMIERE
                            + "Death-Star-II_b5760154.jpeg?region=0%2C0%2C2160%2C1215&width=768")
                    .setTag(Category.values()[3])
                    .setCreationDate(DATE)
                    .setEndDate(DATE)
                    .setUuid("10").build(),

            new Offer.Builder().setUserId("3")
                    .setTitle("Student job in a farm")
                    .setDescription("I need help to rebuild my spaceship in exchange I can help "
                            + "with farm work since I'm pretty good at farm stuff !")
                    .setLinkPicture("https://www.jedidefender.com/collect92/lukexwing%20anh.jpg")
                    .setUuid("11").build(),

            new Offer.Builder().setUserId("17")
                    .setTitle("Lightsaber to find Jedi")
                    .setDescription("I'm looking for a bold general, care to help me ?")
                    .setUuid("12").build(),

            new Offer.Builder().setUserId("2")
                    .setTitle("Transport some important data")
                    .setDescription("I have some nice crew of people that can help you "
                            + "do anything if you are okay to transport some data !")
                    .setUuid("13").setIsClosed(true).build(),

            new Offer.Builder().setUserId("14")
                    .setTitle("Help to fix me up")
                    .setDescription("My torso lacks my legs, I might trade "
                            + "my double ended lightsaber if you're nice.")
                    .setLinkPicture(WIKIA_NO_COOKIE
                            + "a2a264e8-38e0-4c5e-b11d-7232c1f808ce/"
                            + "scale-to-width-down/800")
                    .setUuid("14").setIsClosed(true).build(),

    };

    private static final User[] USER_ZERO_FOLLOWS = {getUser(0), getUser(1), getUser(2)};

    /**
     * Create a FakeDatabase filled with users and offers.
     */
    protected FilledFakeDatabase() {
        super(true);
        for (User user : FAKE_USERS) {
            write(Database.USERS_PATH, user.getUserId(), new User(user));
        }
        for (Offer offer : FAKE_OFFERS) {
            write(Database.OFFERS_PATH, offer.getUuid(), offer);
        }
        for (User user : USER_ZERO_FOLLOWS) {
            String uid = user.getUserId();
            write(Database.FOLLOWERS_PATH + "/" + getUser(0).getUserId(), uid, uid);
        }
    }

    /**
     * Return an user from the list.
     *
     * @param index the index of the user.
     * @return the user at this index
     */
    public static User getUser(int index) {
        return new User(FAKE_USERS[index]);
    }

    /**
     * Return an offer from the list.
     *
     * @param index the index of the offer.
     * @return the offer at this index
     */
    public static Offer getOffer(int index) {
        return FAKE_OFFERS[index];
    }

    /**
     * Return an offer written by a user.
     *
     * @param userId the id of the user
     * @return an offer they wrote or null if they did not write any
     */
    public static Offer getOfferOfUser(String userId) {
        for (Offer o : FAKE_OFFERS) {
            if (o.getUserId().equals(userId)) {
                return o;
            }
        }
        return null;
    }

    /**
     * Return a follower of the user with id 0.
     *
     * @param index the index of the follower
     * @return the follower at this index
     */
    public static User getFollowerOfUserZero(int index) {
        return USER_ZERO_FOLLOWS[index];
    }

    /**
     * The number of offers.
     *
     * @return number of offers
     */
    public static int numberOffer() {
        return FAKE_OFFERS.length;
    }

    /**
     * The number of users.
     *
     * @return number of users
     */
    public static int numberUser() {
        return FAKE_USERS.length;
    }

    /**
     * Return the number of followers of user with id zero.
     *
     * @return the number of followers of user with id zero
     */
    public static int numberFollowersOfUserZero() {
        return USER_ZERO_FOLLOWS.length;
    }

    private static Location getLocation() {
        Location location = new Location("");
        location.setLatitude(FAKE_LATITUDE);
        location.setLongitude(FAKE_LONGITUDE);
        return location;
    }

    /**
     * Returns a list of all closed offer in the database.
     *
     * @return the list of all offers such that their isClosed attribute is true.
     */
    public static List<Offer> getClosedOffers() {
        List<Offer> closedOffers = new ArrayList<>();
        for(Offer offer : FAKE_OFFERS) {
            if (offer.getIsClosed()) {
                closedOffers.add(offer);
            }
        }
        return closedOffers;
    }
}
