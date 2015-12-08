package edu.osu.cse.todolist.to_dolist;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.util.Log;

/**
 * Unit test for ToDoLab singleton centralized data class
 */
public class ToDoLabTest extends AndroidTestCase {
    final private String TAG = "AndroidTestCase";

    /**
     * a flag indicate if it's the first time running setUp() method, so we can do some one-time
     * fixture.
     * <p/>
     * AndroidTestCase does not support annotation or method like @BeforeClass in JUnit 4.
     */
    private static boolean mInitialized = false;

    private RenamingDelegatingContext mContext;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mContext = new RenamingDelegatingContext(getContext(), "test_db");
        // pass the context to ToDoLab
        ToDoLab.get(mContext);

        // populate some test data at the first time running test case
        if (!mInitialized) {
            Log.d(TAG, "[+] Populate Date");
            ToDoLab.get(mContext).populateTestData(10);
            mInitialized = true;
        }

        Log.d(TAG, "setup() called");
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        Log.d(TAG, "tearDown() called");
    }

    /**
     * Test ToDoLab.saveTask() method
     *
     * @throws Exception
     */
    public void testSaveTask() throws Exception {
        ToDoLab toDoLab = ToDoLab.get(mContext);

        Task task = new Task();
        task.setTitle("test1 task");
        // get the total number of tasks before saving
        int num = toDoLab.getTasks().size();

        Log.d(TAG, "# of tasks before saving: " + num);

        // save a task
        assertTrue("Failed to save Task", toDoLab.saveTask(task));
        toDoLab.saveTask(task);
        // check total number of tasks after saving
        assertEquals("Failed to save task", num + 1, toDoLab.getTasks().size());

        Log.d(TAG, "# of tasks after saving: " + toDoLab.getTasks().size());
    }

    public void testDeleteTask() throws Exception {
        ToDoLab toDoLab = ToDoLab.get(mContext);

        // get the total number of tasks before deleting
        int num = toDoLab.getTasks().size();
        Log.d(TAG, "# of tasks before deleting: " + num);
        // if exists tasks, delete the first task. Avoiding delete null
        if (num > 0) {
            Task task = toDoLab.getTasks().get(0);
            toDoLab.deleteTask(task);
            // check total number of tasks after deleting
            assertEquals("Failed to save task", num - 1, toDoLab.getTasks().size());
            Log.d(TAG, "# of tasks after deleting: " + toDoLab.getTasks().size());
        }
    }
}
