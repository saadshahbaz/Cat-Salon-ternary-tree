package asgn;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;


public class CatTree implements Iterable<CatInfo>{
    public CatNode root;
    
    public CatTree(CatInfo c) {
        this.root = new CatNode(c);
    }
    
    private CatTree(CatNode c) {
        this.root = c;
    }
    
    
    public void addCat(CatInfo c)
    {
        this.root = root.addCat(new CatNode(c));
    }
    
    public void removeCat(CatInfo c)
    {
        this.root = root.removeCat(c);
    }
    
    public int mostSenior()
    {
        return root.mostSenior();
    }
    
    public int fluffiest() {
        return root.fluffiest();
    }
    
    public CatInfo fluffiestFromMonth(int month) {
        return root.fluffiestFromMonth(month);
    }
    
    public int hiredFromMonths(int monthMin, int monthMax) {
        return root.hiredFromMonths(monthMin, monthMax);
    }
    
    public int[] costPlanning(int nbMonths) {
        return root.costPlanning(nbMonths);
    }
    
    
    
    public Iterator<CatInfo> iterator()
    {
        return new CatTreeIterator();
    }
    
    
    class CatNode {
        
        CatInfo data;
        CatNode senior;
        CatNode same;
        CatNode junior;
        
        public CatNode(CatInfo data) {
            this.data = data;
            this.senior = null;
            this.same = null;
            this.junior = null;
        }
        
        public String toString() {
            String result = this.data.toString() + "\n";
            if (this.senior != null) {
                result += "more senior " + this.data.toString() + " :\n";
                result += this.senior.toString();
            }
            if (this.same != null) {
                result += "same seniority " + this.data.toString() + " :\n";
                result += this.same.toString();
            }
            if (this.junior != null) {
                result += "more junior " + this.data.toString() + " :\n";
                result += this.junior.toString();
            }
            return result;
        }
        
        
        public CatNode addCat(CatNode c) {
        	
        	root = insert(this, c.data);
        	return root;
        	
        }
        
        private CatNode insert(CatNode root, CatInfo key) {
        	if (root==null) {
        		root = new CatNode(key);
        		return root;
        	}
        	
        	if (key.monthHired < root.data.monthHired) {
        		root.senior = insert(root.senior,key);
        	}
        	else if(key.monthHired > root.data.monthHired) {
        		root.junior = insert(root.junior,key);
        	}
        	else if (key.monthHired == root.data.monthHired) {
        		root.same = insert(root.same, key);
        		
        		if (root.data.furThickness < key.furThickness) {
        			CatInfo temp = root.same.data;
					root.same.data = root.data;
					root.data = temp;
					

        		}
        	}
        	return root;
        }

        public CatNode removeCat(CatInfo c) {
        	root = deleteNode(this, c);
        	return root;

        }
      private CatNode deleteNode(CatNode root, CatInfo key) {
    	  //pointer to start parent node of current node
    	  CatNode parent = null;
    	  
    	  //start with root node
    	  CatNode curr = root;
    	  
    	  //searching the tree for the element
    	  while (curr!=null && curr.data != key) {
    		  parent=curr;
    		  
    		  //if given key is less, so going to the left side 
    		  if (key.monthHired < curr.data.monthHired) {
    			  curr = curr.senior;
    		  }
    		  else if (key.monthHired > curr.data.monthHired) {
    			  curr= curr.junior;
    		  }
    		  else if (key.monthHired == curr.data.monthHired) {
    			  curr = curr.same;
    		  }
    		  
    	  }
    	//case 1: not found
    	  if (curr == null) {
    		  return root;
    	  }
    	  //case 2 leaf nodes
    	  if (curr.senior==null && curr.junior == null && curr.same==null) {
    	  
    	 
	    	  
	    	  if (curr !=root) {
	    		  if (parent.senior == curr) {
	    			  parent.senior=null;
	    		  }
	    		  else if (parent.junior == curr) {
	    			  parent.junior = null;
	    		  }
	    		  else {
	    			  parent.same = null;
	    		  }
	    		  
	    	  }
    	  //if tree has only one node, delete it and set root to null
    	  else {
    		  root=null;
    		  
    	  }
    	  
      }
    	  else if (curr.same != null ) {//&& (curr.senior !=null || curr.junior !=null)) {
    		  CatNode junior = curr.junior;
    		  CatNode senior = curr.senior;
    		 
    		 if (curr!= root) {
    			 if (curr == parent.senior) {
    				 parent.senior = curr.same;
    				 
    				 curr.same.senior =senior;
    				 curr.same.junior=junior;
    				 
    				 curr.same = null;
    				 curr.junior=null;
    				 curr.senior=null;
    			 }
    			 else if(curr == parent.same) {
    				 parent.same = curr.same;
    				 curr.same = null;
    			 }
    			 
    			 else {
    				 parent.junior = curr.same;
    				 curr.same.senior = senior;
    				 curr.same.junior=junior;
    				 
    				 curr.same = null;
    				 curr.junior=null;
    				 curr.senior=null;
    				 
    			 }
    		 }
    		 
    		 
    		 else {
    			 root = curr.same;
    			 curr.same.senior = senior;
    			 curr.same.junior = junior;
    			 
    			 curr.same = null;
				 curr.junior=null;
				 curr.senior=null;
    		 }
  
    		  
    	  }

    	  
    	  //incase of 2 children
    	  
    	  else if (curr.senior != null && curr.junior!=null) {
    		CatNode newRoot = curr.senior;
    		CatNode junior = curr.junior;
    		curr.senior = null;
    		
    		CatNode temp = newRoot.junior;
    		
    		while(temp.junior !=null) {
    			temp = newRoot.junior;
    		}
    		
    		temp.junior = junior;
    		curr.junior = null;
    		
    		return newRoot;
    	  }
    	  //incase of three children
    	 
    	  else {
    	  //with only one child 
    	  CatNode child = (curr.senior !=null)? curr.senior: curr.junior;
    	  
    	  if (curr != root) {
    		  if (curr == parent.senior) {
    			  parent.senior = child;
    		  }
    		  else {
    			  parent.junior = child;
    		  }
    	  }
    	  else {
    		  root = child;
    	  }
    	  
    	  }
    	  return root;
    	  
    	  
    }

        public int mostSenior() {
        	CatNode x = root;
        	while (x.senior!= null) {
        		x=x.senior;
        	}
        	return x.data.monthHired;
        }
        
        public int fluffiest() {
            int max = this.data.furThickness;
            
            int x = maxOfRightElement(this);
            int y = maxOfLeftElement(this);
    
            if (max < y) {
            	max =y;
            }
             if (max < x) {
            	max = x;
            }
            
            return max;

        }

        
        private int maxOfRightElement(CatNode x) {
        	int max = Integer.MIN_VALUE;
        	
        	//if tree is empty
        	if (x== null) {
        		return 0;
        	}
        	
        	//if right child exist
        	
        	if (x.junior != null) {
        		max = x.junior.data.furThickness;
        	}
        	
        	//returning maximum of the right elements
        	return Math.max(maxOfRightElement(x.junior), Math.max(max, maxOfRightElement(x.senior)));
        }
        
        private int maxOfLeftElement(CatNode x) {
        	int max =Integer.MIN_VALUE;
        	
        	if (x==null) {
        		return 0;
        	}
        	if (x.senior != null) {
        		max = x.senior.data.furThickness;
        	}
        	
        	//returning maximum of the right elements
        	return Math.max(maxOfLeftElement(x.senior), Math.max(max, maxOfLeftElement(x.junior)));
        }  
        public int hiredFromMonths(int monthMin, int monthMax) {
        	
        	if (monthMin > monthMax) {
        		return 0;
        	}
        	
        	int x = hired(this, monthMin, monthMax);
        	return x;
            
        }
        
        private int hired(CatNode n, int min, int max) {
        	if (n == null) {
        		return 0;
        	}
        	
        	if (n.data.monthHired >= min && n.data.monthHired <= max) {
        		return 1 + hired(n.senior, min,max) + hired(n.junior, min,max) + hired(n.same, min,max);
        	}
        	else if (n.data.monthHired < min) {
        		return hired(n.junior,min,max);
        	}
        	else {
        		return hired(n.senior, min , max);
        	}
        	
        }
       
        
        public CatInfo fluffiestFromMonth(int month) {
        	CatNode x = this;
        	 while ((x!=null) && (x.data.monthHired != month)) {
        		 //CatNode y = x;
        		 if (month > x.data.monthHired) {
        			 x=x.junior;
        			 
        		 }
        		 else{
        			 x=x.senior;
        		 }
        		 
        		
        	 }
        	 if (x==null) {
        		 return null;
        	 }
        	 

        	 CatInfo max = x.data;
        	 while(x.same!=null) {
        		 if (x.data.furThickness < x.same.data.furThickness) {
        			 max = x.same.data;
        		 }
        		 x = x.same;
        	 }
        	 return max;
        }
        
        public int[] costPlanning(int nbMonths) {
            int[] x = new int[nbMonths];
        
            CatTree tree = new CatTree(this);
          
 
            int i = 0;
            int currmonth=243;
            while (i< x.length) {
            	for (CatInfo info : tree) {
            		if (info.nextGroomingAppointment == currmonth) {
            			x[i] += info.expectedGroomingCost;
            		}
            	}
	
            	i++;
            	currmonth++;
            }
         
            return x; 
        }


        
    }
    
    private class CatTreeIterator implements Iterator<CatInfo> {
        // HERE YOU CAN ADD THE FIELDS YOU NEED
    	
    	CatInfo c;
    	int index;
    	ArrayList<CatInfo> iterList;
    	
        
        public CatTreeIterator() {
           
        	ArrayList<CatInfo> iterList = new ArrayList<CatInfo>();
        	this.iterList = orderedList(root, iterList);
        	this.index = 0;
        	this.c = iterList.get(0);
        	
        	//this.iterList
        }
        private ArrayList<CatInfo> orderedList(CatNode n, ArrayList<CatInfo> iterList){
        	if (n!=null) {
        		orderedList(n.senior, iterList);
        		orderedList(n.same,iterList);
        		iterList.add(n.data);
        				
        			//}
        		orderedList(n.junior,iterList);
        		}
        	return iterList;
        }
        
        public CatInfo next(){
            
            CatInfo temp = this.c ; 
            
            if (index ==iterList.size()-1) {
            	this.c = null;
            } else {
            	this.c = this.iterList.get(this.index+1);
            }
            
            index++;
            return temp;
        }
        
        public boolean hasNext() {
         
            return (c !=null); 
        }
  
    }

 
}


