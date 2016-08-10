package com.discworld.guitarsongviewer.dto;

/**
*
*/

import java.util.List;

//import android.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
* The <code>PagerAdapter</code> serves the fragments when paging.
* @author mwho
*/
public class CPagerAdapter extends FragmentPagerAdapter 
{
   private List<Fragment> fragments;
  /**
   * @param fm
   * @param fragments
   */
   public CPagerAdapter(FragmentManager fm, List<Fragment> fragments) 
  {
     super(fm);
     this.fragments = fragments;
  }
  /* (non-Javadoc)
   * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
   */
  @Override
  public Fragment getItem(int position) 
  {
     return this.fragments.get(position);
  }
  
  public void addItem(Fragment oFragment)
  {
     fragments.add(oFragment);
  }
  
  public void updateItem(Fragment oFragment, int iNdx)
  {
     fragments.set(iNdx, oFragment);
  }

  public void deleteItem(int iNdx)
  {
//     Fragment f = getItem(iNdx);
     
//     FragmentManager manager = f.getFragmentManager();
//     android.support.v4.app.FragmentTransaction trans = manager.beginTransaction();
//     trans.remove(f);
//     trans.commit(); 
     fragments.remove(iNdx);
  }
  
  public void deleteAllItems()
  {
     int iFragmentNbr = fragments.size();
     for(int i = iFragmentNbr - 1; i >= 0 ; i--)
        deleteItem(i);
  }
  
  /* (non-Javadoc)
   * @see android.support.v4.view.PagerAdapter#getCount()
   */
  @Override
  public int getCount() 
  {
     return this.fragments.size();
  }

  /* (non-Javadoc)
   * @see android.support.v4.app.FragmentPagerAdapter#destroyItem(android.view.ViewGroup, int, java.lang.Object)
   */
//  @Override
//  public void destroyItem(ViewGroup container, int position, Object object)
//  {
//     // TODO Auto-generated method stub
//     super.destroyItem(container, position, object);
//     
//     if (position <= getCount()) 
//     {
//        FragmentManager manager = ((Fragment) object).getFragmentManager();
//        android.support.v4.app.FragmentTransaction trans = manager.beginTransaction();
//        trans.remove((Fragment) object);
//        trans.commit();
//    }      
//  }
  
  
}