/*
 *Created by ahmad chaaban on 24-8-2013 *
 *Define the entity Category with 2 persistence fields and automatic primary key*
 */
package com.accountingmobile;


import com.google.appengine.api.datastore.Key;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Category {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Key key; /*automatic primary key*/
   
   //persistence fields
   private String Name;
   private String Description;
   transient long cat_key; // not persistent because of transient
   transient int cat_id; // not persistent because of transient
   
   //Accessor Methods
   public Key getKey() {
       return key;
   }

   public String getName() {
       return Name;
   }
   public void setName(String Name) {
       this.Name = Name;
   }

   public String getDescription() {
       return Description;
   }
   
   public void setDescription(String Description) {
       this.Description = Description;
   }
   
   public long getCategorykey() {
       return cat_key;
   }
   
   public void setCategorykey(long cat_key) {
       this.cat_key = cat_key;
   }
   
   public int getCategoryId() {
       return cat_id;
   }
   
   public void setCategoryId(int cat_id) {
       this.cat_id = cat_id;
   }
   
   public String toString()
   {
       return( this.Name);
   }
}