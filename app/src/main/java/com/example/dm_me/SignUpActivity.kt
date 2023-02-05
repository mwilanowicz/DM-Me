package com.example.dm_me

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.lang.ref.PhantomReference

private lateinit var editName: EditText
private lateinit var editEmail: EditText
private lateinit var editPassword: EditText
private lateinit var confirmButton: Button
private lateinit var returnButton: Button

private lateinit var mAuth: FirebaseAuth
private lateinit var mDatabaseReference: DatabaseReference

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        supportActionBar?.hide()
        
        mAuth = FirebaseAuth.getInstance()

        editName = findViewById(R.id.edit_name)
        editEmail = findViewById(R.id.edit_email)
        editPassword = findViewById(R.id.edit_password)
        confirmButton = findViewById(R.id.confirm_button)
        returnButton = findViewById(R.id.return_button)

        confirmButton.setOnClickListener{
            val name = editName.text.toString()
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()

            signUp(name, email, password)
        }
        returnButton.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
    private fun signUp(name: String, email: String, password: String){
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    addUserToDatabase(name, email, mAuth.currentUser?.uid!!)
                    val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                    finish()
                    startActivity(intent)
                }
                else {
                    Toast.makeText(this@SignUpActivity, "Error while signing up.", Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun addUserToDatabase(name: String, email: String, uid: String){
        mDatabaseReference = FirebaseDatabase.getInstance().getReference()
        mDatabaseReference.child("user").child(uid).setValue(User(name, email, uid))
    }
}