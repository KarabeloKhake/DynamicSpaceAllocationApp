package com.example.dynamicspaceallocation.activities.student;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.local.UserIdStorageFactory;
import com.example.dynamicspaceallocation.R;
import com.example.dynamicspaceallocation.activities.PersonalDetails;

public class StudentHome extends AppCompatActivity {

    ActionBar actionBar;
    private View mProgressView;
    private View mStudentHomeFormView;
    private TextView tvLoad;
    ImageView ivLibrary, ivQualification;
    TextView tvLibrary, tvQualification, tvStudentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_home_activity);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("\t\t\t\t\tStudent Home");

        mStudentHomeFormView = findViewById(R.id.student_home_form);
        mProgressView = findViewById(R.id.student_home_progress);
        tvLoad = findViewById(R.id.tvLoad);
        ivLibrary = findViewById(R.id.ivLibrary);
        ivQualification = findViewById(R.id.ivQualification);
        tvLibrary = findViewById(R.id.tvLibrary);
        tvQualification = findViewById(R.id.tvQualification);
        tvStudentName = findViewById(R.id.tvStudentName);

        String userObjectId = UserIdStorageFactory.instance().getStorage().get();

        //look for this student user in the mobile's storage
        Backendless.Data.of(BackendlessUser.class).findById(userObjectId, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                //user found
                StringBuilder userNames = new StringBuilder();
                userNames.append(response.getProperty("lastName"));
                userNames.append(" ");
                userNames.append(response.getProperty("firstName"));

                tvStudentName.setText(userNames);
            } //end handleResponse()
            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(StudentHome.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
            } //end handleFault()
        });

        ivQualification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentHome.this, AddEducation.class));
            } //end onClick()
        });
    } //end onCreate()

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.student_home, menu);
        return super.onCreateOptionsMenu(menu);
    } //end onCreateOptionsMenu()

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                showProgress(true);
                tvLoad.setText(R.string.text_logging_student_out);
                //logout student
                Backendless.UserService.logout(new AsyncCallback<Void>() {
                    @Override
                    public void handleResponse(Void response) {
                        showProgress(false);
                        Toast.makeText(StudentHome.this, "Student logged out successfully!!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(StudentHome.this, StudentHome.class));
                        finish();
                    } //end handleResponse()
                    @Override
                    public void handleFault(BackendlessFault fault) {
                        showProgress(false);
                        Toast.makeText(StudentHome.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                    } //end handleFault()
                });
                break;
            case R.id.update:
                //go to another updating activity
//                startActivityForResult(new Intent(StudentHome.this, PersonalDetails.class), UPDATE);
                break;
        } //end switch()

        return super.onOptionsItemSelected(item);
    } //end onOptionsItemSelected()

    //Custom Methods
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mStudentHomeFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mStudentHomeFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mStudentHomeFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
        tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
        tvLoad.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    } //end showProgress()
} //end class StudentHome()
