package com.brunocortez.pokemon.view.list

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.brunocortez.pokemon.R
import com.brunocortez.pokemon.view.ViewState
import com.brunocortez.pokemon.view.form.FormPokemonActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_list_pokemons.*
import kotlinx.android.synthetic.main.include_loading.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class ListPokemonsActivity : AppCompatActivity() {


    private val listPokemonsViewModel: ListPokemonsViewModel by viewModel()

    private val adapter: ListPokemonsAdapter by inject()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_pokemons)

        rvPokemons.adapter = adapter
        rvPokemons.layoutManager = GridLayoutManager(this, 3)

        listPokemonsViewModel.getPokemons()

        listPokemonsViewModel.viewState.observe(this, Observer {
            when (it) {
                is ViewState.Loading -> {
                    containerLoading.visibility = View.VISIBLE
                }
                is ViewState.Failed -> {
                    containerLoading.visibility = View.GONE
                    val message = it.throwable.message
                    if (message != "") Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                }
                is ViewState.Success -> {
                    containerLoading.visibility = View.GONE

                    adapter.setItems(it.data)

                    adapter.setOnClickListener {
                        val telaDeDetalhe = Intent(this, FormPokemonActivity::class.java)
                        telaDeDetalhe.putExtra("POKEMON", it)
                        Toast.makeText(this, it.name, Toast.LENGTH_LONG).show()
                        startActivityForResult(telaDeDetalhe, 1)
                    }
                }
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) { listPokemonsViewModel.getPokemons() }
    }
}
