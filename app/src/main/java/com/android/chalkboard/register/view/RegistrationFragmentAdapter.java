package com.android.chalkboard.register.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class RegistrationFragmentAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 3;
    private RegisterFragment1 registerFragment1;
    private RegisterFragment2 registerFragment2;
    private RegisterFragment3 registerFragment3;

    public RegistrationFragmentAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
        registerFragment1 = RegisterFragment1.NewInstance("Role");
        registerFragment2 = RegisterFragment2.NewInstance("Details");
        registerFragment3 = RegisterFragment3.NewInstance("Password");
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public Fragment getItem(int position){
        switch(position) {
            case 0:
                return registerFragment1;
            case 1:
                return registerFragment2;
            case 2:
                return registerFragment3;
            default:
                return null;
        }
    }

    public RegisterFragment1 getRegisterFragment1(){
        return registerFragment1;
    }

    public RegisterFragment2 getRegisterFragment2(){
        return registerFragment2;
    }

    public RegisterFragment3 getRegisterFragment3(){
        return registerFragment3;
    }
}
