package com.jblearning.candystorev5;

import android.content.Context;
import android.widget.Button;

public class CandyButton extends Button {
  private Candy candy;

  public CandyButton( Context context, Candy newCandy ) {
    super( context );
    candy = newCandy;
  }

  public double getPrice( ) {
    return candy.getPrice( );
  }
}
