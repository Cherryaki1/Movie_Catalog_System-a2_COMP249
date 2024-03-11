import java.io.Serializable;

public class Movie implements Serializable {

    // -----------------------------
    // Attributes
    // -----------------------------

    private int year;
    private String title;
    private int duration;
    private String genres;
    private int rating;
    private int score;
    private String director;
    private String actor1;
    private String actor2;
    private String actor3;

    // -----------------------------
    // Getters
    // -----------------------------

    public int getYear() {
        return year;
    }

    public String getTitle() {
        return title;
    }

    public int getDuration() {
        return duration;
    }

    public String getGenres() {
        return genres;
    }

    public int getRating() {
        return rating;
    }

    public int getScore() {
        return score;
    }

    public String getDirector() {
        return director;
    }

    public String getActor1() {
        return actor1;
    }

    public String getActor2() {
        return actor2;
    }

    public String getActor3() {
        return actor3;
    }

    // -----------------------------
    // Setters
    // -----------------------------

    public void setYear(int year) {
        this.year = year;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setActor1(String actor1) {
        this.actor1 = actor1;
    }

    public void setActor2(String actor2) {
        this.actor2 = actor2;
    }

    public void setActor3(String actor3) {
        this.actor3 = actor3;
    }

    // -----------------------------
    // Constructors
    // -----------------------------

    public Movie() { // Default constructor
        this.year = 0;
        this.title = "";
        this.duration = 0;
        this.genres = "";
        this.rating = 0;
        this.score = 0;
        this.director = "";
        this.actor1 = "";
        this.actor2 = "";
        this.actor3 = "";
    }

    public Movie(int year, String title, int duration, String genres, int rating, int score, String director, String actor1,
            String actor2, String actor3) { // Parameterized constructor
        this.year = year;
        this.title = title;
        this.duration = duration;
        this.genres = genres;
        this.rating = rating;
        this.score = score;
        this.director = director;
        this.actor1 = actor1;
        this.actor2 = actor2;
        this.actor3 = actor3;
    }

    // -----------------------------
    // Overridden methods
    // -----------------------------

    
    @Override
    public String toString() {
        return "Year: " + year + "\nTitle: " + title + "\nDuration: " + duration + "\nGenres: " + genres + "\nRating: "
                + rating + "\nScore: " + score + "\nDirector: " + director + "\nActor 1: " + actor1 + "\nActor 2: "
                + actor2 + "\nActor 3: " + actor3;
    }

    @Override
    public boolean equals(Object newMovie) {
        if (newMovie == null) {
            return false;
        }
        if (newMovie == this) {
            return true;
        }
        if (newMovie.getClass() != this.getClass()) {
            return false;
        }
        Movie movie = (Movie) newMovie;
        return movie.getYear() == this.year && movie.getTitle().equals(this.title)
                && movie.getDuration() == this.duration && movie.getGenres().equals(this.genres)
                && movie.getRating() == this.rating && movie.getScore() == this.score
                && movie.getDirector().equals(this.director) && movie.getActor1().equals(this.actor1)
                && movie.getActor2().equals(this.actor2) && movie.getActor3().equals(this.actor3);
    }

}
