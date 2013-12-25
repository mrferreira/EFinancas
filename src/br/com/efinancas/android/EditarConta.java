package br.com.efinancas.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import br.com.efinancas.android.dao.Conta;
import br.com.efinancas.android.dao.ContaDataSource;

/**
 * @author: Misael Ferreira
 * Date: 15/12/13
 * Time: 08:38
 */
public class EditarConta extends Activity implements View.OnClickListener{

    private Button btnSalvar;
    private EditText txtNome;
    private EditText txtDescricao;
    private EditText txtTipo;
    private EditText txtValorInicial;

    private ContaDataSource contaDataSource;

    long idConta;
    Conta conta;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_conta);

        Intent i = getIntent();
        idConta = i.getLongExtra("idConta", 0);
        Toast.makeText(EditarConta.this,"idConta: " + idConta,Toast.LENGTH_LONG).show();

        btnSalvar = (Button)findViewById(R.id.btnSalvarEC);
        btnSalvar.setOnClickListener(this);

        contaDataSource = new ContaDataSource(this);
        contaDataSource.open();

        if(idConta != 0){
            conta = contaDataSource.getContaById(idConta);

            txtNome = (EditText)findViewById(R.id.txtNomeContaEC);
            txtNome.setText(conta.getNome());
            txtDescricao = (EditText)findViewById(R.id.txtDescricaoContaEC);
            txtDescricao.setText(conta.getDescricao());
            txtTipo = (EditText)findViewById(R.id.txtTipoContaEC);
            //TODO salvar tipo de conta
            txtValorInicial = (EditText)findViewById(R.id.txtValorInicContaEC);
            txtValorInicial.setText(String.valueOf(conta.getValorInicial()));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSalvarEC:
                txtNome = (EditText)findViewById(R.id.txtNomeContaEC);
                txtDescricao = (EditText)findViewById(R.id.txtDescricaoContaEC);
                txtTipo = (EditText)findViewById(R.id.txtTipoContaEC);
                txtValorInicial = (EditText)findViewById(R.id.txtValorInicContaEC);

                if(conta == null){
                    conta = new Conta();
                }

                conta.setNome(txtNome.getText().toString());
                conta.setDescricao(txtDescricao.getText().toString());
                conta.setValorInicial(Double.valueOf(txtValorInicial.getText().toString()));

                if(conta.getId() == 0){
                    contaDataSource.createConta(conta);
                    Toast.makeText(EditarConta.this,"criando nova conta " + conta.getNome(),Toast.LENGTH_LONG).show();
                }else{
                    contaDataSource.updateConta(conta);
                    Toast.makeText(EditarConta.this,"atualizanco conta " + conta.getNome(),Toast.LENGTH_LONG).show();
                }

                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setClass(EditarConta.this,MyActivity.class);
                startActivity(intent);

                break;
        }
    }

    private void log(String msg){
        Log.d("eFinancasAndroid", msg);
    }
}
