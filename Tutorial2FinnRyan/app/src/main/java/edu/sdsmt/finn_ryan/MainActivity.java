/*

 Tutorial 2 Grading

 Complete the following checklist.


 __X__	10pt 	T2: Package is named correctly (not com.example.puzzle)

 __X__	10pt 	T2: The layout items are named correctly (grubbyImage, buttonStart, textWelcome)

 __X__	50pt	T2: Tutorial is completed

 __X__	15pt 	T2: TASK: Custom view covered entire screen

 __P__	15pt 	T2: TASK: Landscape task

 __X__	15pt 	T2: CSC 578 ONLY TASK: Border around the board area



 The checklist is the starting point for course staff, who reserve the right to change the grade if they disagree with your assessment and to deduct points for other issues they may encounter, such as errors in the submission process, naming issues, etc.

 */
/*
 * I'm having trouble with the landscape view. The design seems fine, but in emulation (5.1 WVGA API 23)
 * and on my phone (samsung SM-G991U) rotating into landscape will show only the button with no image.
 * The text field also won't show up in either orientation when in emulation or on phone.
 */

package edu.sdsmt.finn_ryan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onStartPuzzle(View view) {
        Intent intent = new Intent(this, PuzzleActivity.class);
        startActivity(intent);
    }
}
