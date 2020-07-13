package servlet;

public interface Routable {
    /**
     * Identify which page do you want to attend (Ex. login, home)
     */
    String getPattern();
}
