package com.rakangsoftware.users.screen.users

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.rakangsoftware.users.R
import com.rakangsoftware.users.data.user.User
import com.rakangsoftware.users.databinding.UsersActivityBinding
import kotlinx.android.synthetic.main.users_activity.*

class UsersActivity : AppCompatActivity() {

    lateinit var viewModel: UsersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this, UsersViewModelFactory(this)).get(UsersViewModel::class.java)
        val binding: UsersActivityBinding = DataBindingUtil.setContentView(this, R.layout.users_activity)
        binding.viewModel = viewModel;
        binding.setLifecycleOwner(this)
        binding.executePendingBindings()

        val adapter = UserAdapter()
        adapter.setOnUserClickListener(object : UserViewHolder.OnUserClickedListener {
            override fun onUserClicked(user: User) {
                viewModel.deleteUser(user)
            }
        })

        user_list.layoutManager = LinearLayoutManager(this)
        user_list.adapter = adapter

        add_user.setOnClickListener {
            showCreateDialog(adapter)
        }
    }

    private fun showCreateDialog(adapter: UserAdapter) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add user")

        val view = layoutInflater.inflate(R.layout.user_create_dialog, null)
        builder.setView(view)

        val firstNameView = view.findViewById(R.id.firstName) as TextInputEditText
        val lastNameView = view.findViewById(R.id.lastName) as TextInputEditText

        builder.setPositiveButton(android.R.string.ok) { dialog, p ->
            viewModel.createUser(User(firstNameView.text.toString(), lastNameView.text.toString()))
        }

        builder.show()
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, UsersActivity::class.java))
        }
    }
}
