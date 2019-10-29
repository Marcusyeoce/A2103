/* This is just a rough idea of how to transform the BSTDemo.java under your lect10_programs
 * So you can and should reference that while doing your AVL tree
 * The difference is that instead of the input is an int (the only attribute in the BSTVertex)
 * the input will be a AVLVertex itself
 *
 * The AVL code uses a lot of recursion and overloading
 *
 * There are other ways to implement an AVLTree, for example some might prefer to just make a generic AVLNode and have another class
 * as the attribute 'key' and just compare the .key for ordering. That works too and it would follow more closely to the BSTDemo.java
 * where you can literally copy and have 99% of your code done. 
 *
 * The code might have some minor errors as I did not try to compile it, so make any changes you need
*/

class AVLVertex implements Comparable<AVLVertex>{
  public AVLVertex parent, left, right;
  public int key;
  public int height;
  public int size;

  AVLVertex() {
     parent = left = right = null;
     height = 0;
     size = 1; // This wasn't included in the BSTVertex but is required here
   }

   public int compareTo(AVLVertex o) {} // As you need an ordering for your vertices, it is easier to implement here rather than create a new comparable class
}

class AVL {
  /* Every action that is performed starts from the root
   * So try to visualize how you would take the action
   * It could help your implementation
   */
  public AVLVertex root;

  public AVL() {root = null;}

  // Searching is just going left/right till you find the right node
  public AVLVertex search(AVLVertex v) {}
  public AVLVertex search(AVLVertex T, AVLVertex v) {}

  // Finding the min and max is just going left or right all the way
  public AVLVertex findMin() {}
  public AVLVertex findMin(AVLVertex T) {}
  public AVLVertex findMax() {}
  public AVLVertex findMax(AVLVertex T) {}

  /* The successor of a node is simply the immediate next larger node
   * The successor would be the minimum in the subtree of the right child if there is a right children
   * Otherwise go up the parent till the first right turn
   */
   //public AVLVertex successor(AVLVertex v) {
   //  AVLVertex vPos = search(root, v);
   //  return vPos == null ? null : successor(vPos);
   //}

   public AVLVertex successor(AVLVertex T) {
     if (T.right != null)                       // this subtree has right subtree
       return findMin(T.right);  // the successor is the minimum of right subtree
     else {
       AVLVertex par = T.parent;
       AVLVertex cur = T;
       // if par(ent) is not root and cur(rent) is its right children
       while ((par != null) && (cur == par.right)) {
         cur = par;                                         // continue moving up
         par = cur.parent;
       }
       return par == null ? null : par;           // this is the successor of T
     }
   }

   /* The predecessor of a node is just the immediate next smaller node
    * The implementation is just the opposite of successor
    * You can pass the THL without this function but it would be a good practice to implement it
   */
   public AVLVertex predecessor(AVLVertex V) {}
   public AVLVertex predecessor(AVLVertex T)

   // Inorder traversal is just one of the three tree traversals
   // Use it to check the implementation of your AVL
   public void inorder() {//left, print, right}

   /* To insert a node, just traverse down a tree till you find a suitable spot to insertion
    * Since this is a AVL, you will have to update your height and size and then balance
    */
   public void insert(AVLVertex V) {
     root = insert(root,V)
   }

   public AVLVertex insert(AVLVertex T, AVLVertex v) {
     if (T == null) return new AVLVertex(v);          // insertion point is found

     if (T.CompareTo(v) > 0) { //search left
       T.left = insert(T.left, v);
       T.left.parent = T;
     }
     else { //search right
       T.right = insert(T.right, v);
       T.right.parent = T;
     }

     // Update height and size
     // T.size = ?
     // T.height = ?

     return balance(T)
   }

   /* The reason why we need the following 2 functions is because you can't simply call
    * leftChild.size or leftchild.height as it is not guaranteed that the node will always
    * have a left or right child. When that happens you will run into nullpointerexception
    */
   public int size(AVLVertex V) {
     if (V == null) {return 0;}
     return V.size;
   }

   public int height(AVLVertex V) {
     if (V == null) {return -1;}
     return V.height;
   }

   /* To delete a node we have to first search down the tree then replace it with
    * its successor or predecessor. The difference here is that instead of just changing
    * the attribute value like in the BSTDemo, it is fully replaced here
    */
    public void delete(AVLVertex v) {
      root = delete(root, v);
    }

    // helper recursive method to perform deletion
    public AVLVertex delete(AVLVertex T, AVLVertex v) {
      if (T == null)  return T;              // cannot find the item to be deleted

      if (T.CompareTo(v) < 0)                                    // search to the right
        T.right = delete(T.right, v);
      else if (T.CompareTo(v) > 0)                               // search to the left
        T.left = delete(T.left, v);
      else {                                            // this is the node to be deleted
        if (T.left == null && T.right == null)                   // this is a leaf
          T = null;                                      // simply erase this node
        else if (T.left == null && T.right != null) {   // only one child at right
          T.right.parent = T.parent;
          T = T.right;                                                 // bypass T
        }
        else if (T.left != null && T.right == null) {    // only one child at left
          T.left.parent = T.parent;
          T = T.left;                                                  // bypass T
        }
        else {                                 // has two children, find successor
          AVLVertex temp = T;
          T = successor(v);
          T.right = delete(T.right,T);
          T.left = temp.left;
        }
      }

      // Update height and size
      // T.size = ?
      // T.height = ?

      return balance(T);                                          // return the updated BST
    }

    /* To balance a (portion of) tree, you need to check the height difference of the
     * 2 child and from there determine if you need a L, LR, R or RL rotations.
     * Check the conditions in your notes and familiarise yourself with it.
     * It's also good to write a function to calculate the balance factor instead
     * of writing it 4 long if-conditions
     */
    public AVLVertex balance(AVLVertex T) {
      if (balFac(T) > 1) {
        if (balFac(T.left) < 0) {
          // rotateLeft T.left
        }
        // rotateRight T
      }

      if (balFac(T) < -1) {
        if (balFac(T) > 0) {
          // rotateRight T.right
        }
        // rotateLeft T
      }

      return T;
    }

    public int balFac(AVLVertex T) {
  		return height(T.left) - height(T.right);
  	}

    // Rotate left and right are available in your notes
    public AVLVertex rotateRight(AVLVertex T) {}
    public AVLVertex rotateLeft(AVLVertex T) {}

    // Rank will be needed for this THL
    public int rank(AVLVertex V) {
      return rank(root,V);
    }

    public int rank(AVLVertex T, AVLVertex V){
      if (T == null) { //If the vertex isn't in the tree
        return -1;
      }
      // if T > S, just go left
      // if T < S, go right and add 1 + size(T.left) to rank
      // if T == S, return size(T.left) + 1
    }
}
