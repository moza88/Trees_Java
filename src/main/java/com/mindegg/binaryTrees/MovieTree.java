package com.mindegg.binaryTrees;


class Movie{
    String title;
    int release_date;
    Movie leftChild;
    Movie rightChild;

    Movie(String title, int release_date){
        this.title = title;
        this.release_date = release_date;
    }

    public String toString() {
        return title + " came out on " + release_date;
    }
}

public class MovieTree {

    Movie root;

    /**
     * Adding in a Movie into a Binary Tree
     *
     * @param title
     * @param release_date
     */
    public void addMovie(String title, int release_date){

        //Initialize the new movie you are adding in
        Movie newMovie = new Movie(title, release_date);

        //If there isn't even a root then this movie will be the root of the tree
        if(root == null) {
            root = newMovie;

        } else {
            //We need to traverse through the tree to find the right place
            //to add this new movie, will be according to its release date
            Movie currentMovie = root;
            //Future parent of our new Movie node
            Movie parent;

            while(currentMovie != null) {

                parent = currentMovie;

                //Checking if the new movie belongs to the left side
                if(release_date < currentMovie.release_date) {

                    currentMovie = currentMovie.leftChild;

                    //The left child has no children so we are at the end of this branch
                    if(currentMovie == null) {

                        //place the new movie to the left of it
                        parent.leftChild = newMovie;
                        return; //All Done
                    }
                //Now we are checking if the new movie belongs to the right side
                } else {

                    currentMovie = currentMovie.rightChild;

                    if(currentMovie == null) {
                        parent.rightChild = newMovie;
                        return;
                    }
                }
            }

        }
    }

    public boolean deleteMovie(int release_date) {
        //Searching from the top of the tree
        Movie currentMovie = root;
        Movie parent = root;

        boolean isAtLeftChild = true;
        //Search for the node and mark it as a leftChild or rightChild
        while(currentMovie.release_date != release_date) {
            parent = currentMovie;

            //Move to the left if the date is less than the current movie's date
            if(currentMovie.release_date > release_date) {
                currentMovie = currentMovie.leftChild;
                isAtLeftChild = true;

            //Move to the right if the date is less than the current movie's date
            } else {
                currentMovie = currentMovie.rightChild;
                isAtLeftChild = false;
            }

            //We were never able to find it so we can't remove anything
            //Get out of this method
            if(currentMovie == null) {
                return false;
            }
        }
        /*Scenario 1: The node has no children
                    [currentMovie]
                    /            \
                  null          null
         */
        if(currentMovie.leftChild == null && currentMovie.rightChild == null) {

            if(currentMovie == root) {
                root = null;

            } else if(isAtLeftChild) {
                parent.leftChild = null;

            } else if(!isAtLeftChild) {
                parent.rightChild = null;
            }
        //Scenario 2: Only node's right child is null
        } else if(currentMovie.rightChild == null) {
            /*
            if this is the scenario:
                  [currentMovie] => root
                    /           \
                  null       L-Child
            then replace the root with the left child
            */
            if (currentMovie == root) {
                root = currentMovie.leftChild;
            /*
            If this is the scenario:
                 [parent]
                  /     \
            otherNode  [currentMovie]
                        /           \
                      null       L-Child
            then replace the current movie with it's left child
            */
            } else if (isAtLeftChild) {
                parent.leftChild = currentMovie.leftChild;
            /*
            if this is the scenario:
                         [parent]
                         /     \
              [currentMovie]   otherNode
              /           \
            null       L-Child
            then replace the current movie with it's left child
            */
            } else {
                parent.rightChild = currentMovie.leftChild;
            }

        //Scenario 3: Only the left child is null
        } else if(currentMovie.leftChild == null) {
            /*
            if this is the scenario:
                  [currentMovie] => root
                    /           \
                 R-child         null
            then replace the root with the left child
            */
            if(currentMovie == root) {
                root = currentMovie.rightChild;
            }
            /*
            If this is the scenario:
                 [parent]
                  /     \
            otherNode  [currentMovie]
                        /           \
                      R-child         null
            then replace the current movie's parent's left child with it's right child
            */
            else if(isAtLeftChild) {
                parent.leftChild = currentMovie.rightChild;
            }
            /*
            if this is the scenario:
                         [parent]
                         /     \
              [currentMovie]   otherNode
              /           \
            R-Child         null
            then replace the current movie's parent's right child with it's left child
            */
            else {
                parent.rightChild = currentMovie.leftChild;
            }
        }

        //Scenario 4: both children are present
        else {

            Movie replacement = getReplacementNode(currentMovie);
            /*
            if this is the scenario:
                  [currentMovie] => root
                    /           \
                  R-Child       L-Child
            then replace the root with the replacement
            */
            if(currentMovie == root) {
                root = replacement;
            /*
            If this is the scenario:
                 [parent]
                  /     \
            otherNode  [currentMovie]
                        /           \
                      R-child         L-child
            then replace the current movie's parent's left child with the replacement
            */
            } else if(isAtLeftChild) {
                parent.leftChild = replacement;
            /*
            if this is the scenario:
                         [parent]
                         /     \
              [currentMovie]   otherNode
                    /   \        \
            R-Child      L-Child   null
            then replace the current movie's parent's right child with the replacement
            */
            } else {
                parent.rightChild = replacement;
            }
            replacement.leftChild = currentMovie.leftChild;
        }
        return true;
    }

    private Movie getReplacementNode(Movie replacedMovie) {
        Movie replacementParent = replacedMovie;
        Movie replacement = replacedMovie;

        Movie currentMovie = replacedMovie.rightChild;

        while(currentMovie != null) {
            replacementParent = replacement;
            replacement = currentMovie;
            currentMovie = currentMovie.leftChild;
        }
        if(replacement != replacedMovie.rightChild) {
            replacementParent.leftChild = replacement.rightChild;
            replacement.rightChild = replacedMovie.rightChild;
        }
        return replacement;
    }

    /**
     * Find a movie using the movie's release date
     * @param release_date
     * @return
     */
    public Movie findMovie(int release_date) {

        Movie currentMovie = root;

        //If we don't have a match then we need to keep searching
        while(currentMovie.release_date != release_date) {

            if(currentMovie.release_date > release_date) {
                currentMovie = currentMovie.leftChild;

                if(currentMovie == null) {
                    return null;
                }
            } else {
                currentMovie = currentMovie.rightChild;

                if(currentMovie == null) {
                    return null;
                }
            }
        }
        return currentMovie;
    }

    /**
     * Prints the tree in ascending order by the release date
     *
     * Example of DFS (Depth First Search)
     *
     * @param currentMovie
     */
    public void inOrderTraversal(Movie currentMovie) {

        if(currentMovie != null) {
            inOrderTraversal(currentMovie.leftChild);

            System.out.println(currentMovie);

            inOrderTraversal(currentMovie.rightChild);
        }
    }

    /**
     * Prints the tree by going to the parent, left, and then right
     *
     * Example of DFS (Depth First Search)
     *
     * @param currentMovie
     */
    //Prints the tree by going to the parent, left, and then right
    public void preOrderTraversal(Movie currentMovie) {

        if(currentMovie != null) {
            System.out.println(currentMovie);

            preOrderTraversal(currentMovie.leftChild);

            preOrderTraversal(currentMovie.rightChild);
        }
    }

    /**
     * Prints the tree by going to the left, right children first, the parent
     *
     * Example of DFS (Depth First Search)
     *
     * @param currentMovie
     */
    //Prints the tree by going to the left, right children first, the parent
    //Starts on the bottom and comes up to th root
    public void postOrderTraversal(Movie currentMovie) {

        if(currentMovie != null) {
            postOrderTraversal(currentMovie.leftChild);

            postOrderTraversal(currentMovie.rightChild);

            System.out.println(currentMovie);
        }
    }

    public static void main(String[] args) {

        MovieTree movieTree = new MovieTree();

        movieTree.addMovie("Shashank Redemption", 1992);
        movieTree.addMovie("Finding Nemo", 2004);
        movieTree.addMovie("Black Panther", 2018);
        movieTree.addMovie("The Godfather", 1977);
        movieTree.addMovie("I Care", 2020);

        System.out.println("\nIn-Order Traversal");
        movieTree.inOrderTraversal(movieTree.root);

        System.out.println("\nPre-Order Traversal");
        movieTree.preOrderTraversal(movieTree.root);

        System.out.println("\nPost-Order Traversal");
        movieTree.postOrderTraversal(movieTree.root);

        System.out.println("Is there a movie from 2021?");
        movieTree.findMovie(2021);

        System.out.println("Is there a movie from 2021?");
        System.out.println(movieTree.findMovie(2021));

        System.out.println("Is there a movie from 2018?");
        System.out.println(movieTree.findMovie(2018));

        System.out.println("Remove movie from 2018");
        //movieTree.deleteMovie(2018);

        System.out.println("Is there a movie from 2018?");
        System.out.println(movieTree.findMovie(2018));

        System.out.println("\nPost-Order Traversal");
        movieTree.postOrderTraversal(movieTree.root);
    }
}
