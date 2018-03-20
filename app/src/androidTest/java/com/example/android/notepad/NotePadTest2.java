package com.example.android.notepad;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import org.junit.Test;

@SuppressWarnings("unchecked")
public class NotePadTest2 extends ActivityInstrumentationTestCase2 {
    private static final String NOTE_1 = "Note 1";
    private static final String NOTE_2 = "Note 2";
    private static final String TARGET_PACKAGE_ID = "com.example.android.notepad";
    private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "com.example.android.notepad.NotesList";

    private static Class<?> launcherActivityClass;
    static{
        try {
            launcherActivityClass = Class.forName(LAUNCHER_ACTIVITY_FULL_CLASSNAME);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public NotePadTest2() throws ClassNotFoundException {
        super(TARGET_PACKAGE_ID, launcherActivityClass);
    }

    private Solo solo;

    @Override
    protected void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Test
    public void testAddNote() throws Exception {
        //Unlock the lock screen
        solo.unlockScreen();
        //Click on action menu item add
        solo.clickOnView(solo.getView(com.example.android.notepad.R.id.menu_add));
        //Assert that NoteEditor activity is opened
        solo.assertCurrentActivity("Expected NoteEditor Activity", NoteEditor.class);
        //In text field 0, enter Note 1
        solo.enterText(0, NOTE_1);
        //Click on action menu item Save
        solo.clickOnView(solo.getView(com.example.android.notepad.R.id.menu_save));
        //Click on action menu item Add
        solo.clickOnView(solo.getView(com.example.android.notepad.R.id.menu_add));
        //In text field 0, type Note 2
        solo.typeText(0, NOTE_2);
        //Click on action menu item Save
        solo.clickOnView(solo.getView(com.example.android.notepad.R.id.menu_save));
        //Takes a screenshot and saves it in "/sdcard/Robotium-Screenshots/".
        solo.takeScreenshot();
        //Search for Note 1 and Note 2
        boolean notesFound = solo.searchText(NOTE_1) && solo.searchText(NOTE_2);
        //To clean up after the test case
        deleteNotes();
        //Assert that Note 1 & Note 2 are found
        assertTrue("Note 1 and/or Note 2 are not found", notesFound);
    }

    @Test
    public void testEditNoteTitle() throws Exception {
        //Click on add action menu item
        solo.clickOnView(solo.getView(com.example.android.notepad.R.id.menu_add));
        //In text field 0, enter Note 1
        solo.enterText(0, NOTE_1);
        //Press hard key back button
        solo.goBack();
        solo.clickOnText(NOTE_1);
        //Click on menu item "Edit title"
        solo.clickOnMenuItem("Edit title");
        //Clear the edit text field
        solo.clearEditText(0);
        //In the text field enter Note 2
        solo.enterText(0, NOTE_2);
        //Click on button "OK"
        solo.clickOnButton("OK");
        //Click on action menu item Save
        solo.clickOnView(solo.getView(com.example.android.notepad.R.id.menu_save));
        //Long click Note 2
        solo.clickLongOnText(NOTE_2);
        //Click on Delete
        solo.clickOnText("Delete");
        //Assert that Note 2 is deleted
        assertFalse("Note 2 is found", solo.searchText(NOTE_2));
    }

    private void deleteNotes() {
        //Click on first item in List
        solo.clickInList(1);
        //Click on delete action menu item
        solo.clickOnView(solo.getView(com.example.android.notepad.R.id.menu_delete));
        //Long click first item in List
        solo.clickLongInList(1);
        //Click delete
        solo.clickOnText(solo.getString(R.string.menu_delete));
    }

    @Override
    public void tearDown() throws Exception {

        try {
            solo.finalize();
        } catch (Throwable e) {

            e.printStackTrace();
        }
        getActivity().finish();
        super.tearDown();

    }

}