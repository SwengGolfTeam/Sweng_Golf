package ch.epfl.sweng.swenggolf.database;

import ch.epfl.sweng.swenggolf.User;
import ch.epfl.sweng.swenggolf.offer.Offer;

public final class FilledFakeDatabase extends FakeDatabase {

    /**
     * Returns a prefilled FakeDatabase.
     */
    protected FilledFakeDatabase() {
        super(true);
        for (User user : FAKE_USERS) {
            write("/users", user.getUserId(), new User(user));
        }
        for (Offer offer : FAKE_OFFERS) {
            write("/offers", offer.getUuid(), new Offer(offer));
        }
        for(User user : FOLLOWERS_OF_USER_ZERO){
            String uid = user.getUserId();
            write(Database.FOLLOWERS_PATH + "/" + getUser(0).getUserId(), uid,uid);
        }
    }

    private static final String WIKIA_NO_COOKIE = "https://vignette.wikia.nocookie.net/";
    private static final String LUMIERE = "https://lumiere-a.akamaihd.net/v1/images/";
    private static final String STARWARS = "starwars/";

    /**
     * Return an user from the list.
     * @param index  the index of the user.
     * @return the user at this index
     */
    public static User getUser(int index) {
        return new User(FAKE_USERS[index]);
    }

    /**
     * Return an offer from the list.
     * @param index the index of the offer.
     * @return the offer at this index
     */
    public static Offer getOffer(int index) {
        return new Offer(FAKE_OFFERS[index]);
    }

    /**
     * Return a follower of the user with id 0.
     * @param index the index of the follower
     * @return the follower at this index
     */
    public static User getFollowerOfUserZero(int index) {
        return FOLLOWERS_OF_USER_ZERO[index];
    }

    /**
     * The number of offers.
     * @return number of offers
     */
    public static int numberOffer() {
        return FAKE_OFFERS.length;
    }

    /**
     * The number of users.
     * @return number of users
     */
    public static int numberUser() {
        return FAKE_USERS.length;
    }

    /**
     * Return the number of followers of user with id zero.
     * @return the number of followers of user with id zero
     */
    public static int numberFollowersOfUserZero() {
        return FOLLOWERS_OF_USER_ZERO.length;
    }

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
            new Offer("0", "Little Droid for ride",
                    "I have a little droid with informations"
                            + " on the rebels care to exchange it against"
                            + " a ride from Tatooine ?",
                    WIKIA_NO_COOKIE + STARWARS
                            + "images/f/ff/Sandcrawler.png/"
                            + "revision/latest?cb=20130812001443",
                    "01"),
            new Offer("13", "Defense against the droids",
                    "The Trade Federation is attacking my planet,"
                            + " I need help ! I have some nice clothes"
                            + " I can exchange !",
                    LUMIERE
                            + "databank_battledroid_01_169_1524f145.jpeg?"
                            + "region=0%2C0%2C1560%2C878&width=768",
                    "02"),
            new Offer("8", "Chosen one",
                    "Someone out there is the chosen one ?"
                            + " If you are I can train you !"
                            + " Warning, last apprentice got bad burns !",
                    "https://i.redd.it/4zqd4mvkq3n01.jpg",
                    "03"),
            new Offer("15", "Great Price to find a friend",
                    "I'm looking for a \"friend\" of mine, a certain Han Solo,"
                            + " I offer a desert spaceship to interested !"
                            + " It's him on the left.",
                    "https://cdn3.whatculture.com/"
                            + "images/2014/12/Star-Wars-Special-Edition-Jabba-600x400.jpg",
                    "04"),
            new Offer("7", "Defeat Dark Sidious I must",
                    "Defeat Dark Sidious. "
                            + "With me train you shall,"
                            + " If so you want.",
                    WIKIA_NO_COOKIE + STARWARS
                            + "images/2/23/Gngf.jpg/revision/latest?cb=20080326171911",
                    "05"),
            new Offer("4", "Help to get the senate",
                    "I'm looking for an apprentice to show him"
                            + " my unlimited power and take down the senate !",
                    LUMIERE
                            + "galactic-senate-3_9351812c.jpeg?region=0%2C0%2C800%2C342",
                    "06"),
            new Offer("5", "Prepare Surprise for a friend",
                    "Someone would like to help me prepare a surprise "
                            + "for a friend ? Create a display with \"NOT YET !\""
                            + " on it. I'll invite you to a beer then.",
                    "",
                    "07"),
            new Offer("10", "Take revenge on my apprentice",
                    "Need someone to find my apprentice, some \"bat\" guy. "
                            + "I'll show you the League of Shadows !",
                    "",
                    "08"),
            new Offer("9", "bipbupbap",
                    "titut bip bop tilit tut tut tat dut dut ! Mip zat zat !",
                    LUMIERE
                            + "jawas_42e63e07.jpeg?region=866%2C10%2C1068%2C601&width=768",
                    "09"),
            new Offer("1", "Nice helmet to build ship",
                    "I must build some star like ship, you can have my helmet then !",
                    LUMIERE
                            + "Death-Star-II_b5760154.jpeg?region=0%2C0%2C2160%2C1215&width=768",
                    "010"),
            new Offer("3", "Student job in a farm",
                    "I need help to rebuild my spaceship in exchange I can help "
                            + "with farm work since I'm pretty good at farm stuff !",
                    "https://www.jedidefender.com/collect92/lukexwing%20anh.jpg",
                    "011"),
            new Offer("17", "Lightsaber to find Jedi",
                    "I'm looking for a bold general, care to help me ?",
                    "",
                    "012"),
            new Offer("2", "Transport some important data",
                    "I have some nice crew of people that can help you "
                            + "do anything if you are okay to transport some data !",
                    "",
                    "013"),
            new Offer("14", "Help to fix me up",
                    "My torso lacks my legs, I might trade "
                            + "my double ended lightsaber if you're nice.",
                    WIKIA_NO_COOKIE
                            + "a2a264e8-38e0-4c5e-b11d-7232c1f808ce/"
                            + "scale-to-width-down/800",
                    "014")
    };

    private static final User[] FOLLOWERS_OF_USER_ZERO = {getUser(0), getUser(1), getUser(2)};
}
