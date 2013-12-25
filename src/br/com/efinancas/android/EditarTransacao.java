package br.com.efinancas.android;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import br.com.efinancas.android.dao.Transacao;
import br.com.efinancas.android.dao.TransacaoDataSource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author: Misael Ferreira
 * Date: 14/12/13
 * Time: 15:56
 */
public class EditarTransacao extends Activity {

    Calendar myCalendar = Calendar.getInstance();
    private EditText txtData;
    private EditText txtNome;
    private EditText txtDescricao;
    private EditText txtValor;
    private Button btnSalvar;

    private Transacao transacao;
    private TransacaoDataSource transacaoDataSource;

    int idConta;

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener(){

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.editar_transacao);

        Intent i = getIntent();
        idConta = i.getIntExtra("idConta",0);
        debug("idConta recebido: " + idConta);
        transacaoDataSource = new TransacaoDataSource(EditarTransacao.this);
        transacaoDataSource.open();

        long idTransacao = getIntent().getLongExtra("idTransacao", 0);

        if(idTransacao != 0){
            transacao = transacaoDataSource.getTransacaoById(idTransacao);

            txtData = (EditText)findViewById(R.id.txtDataET);
            txtData.setText(new SimpleDateFormat("dd/MM/yyyy").format(transacao.getData()));
            txtNome = (EditText)findViewById(R.id.txtNomeTransacaoET);
            txtNome.setText(transacao.getNome());
            txtValor = (EditText)findViewById(R.id.txtValorET);
            txtValor.setText(String.valueOf(transacao.getValor()));
            txtDescricao = (EditText)findViewById(R.id.txtDescricaoET);
            txtDescricao.setText(transacao.getDescricao());
        }

        btnSalvar = (Button)findViewById(R.id.btnSalvarET);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtData = (EditText)findViewById(R.id.txtDataET);
                txtNome = (EditText)findViewById(R.id.txtNomeTransacaoET);
                txtValor = (EditText)findViewById(R.id.txtValorET);
                txtDescricao = (EditText)findViewById(R.id.txtDescricaoET);

                if(transacao == null){
                    transacao = new Transacao();
                }

                transacao.setNome(txtNome.getText().toString());
                transacao.setValor(Double.valueOf(txtValor.getText().toString()));
                transacao.setIdTag(0); //TODO configurar TAG
                transacao.setDescricao(txtDescricao.getText().toString());
                String dt = txtData.getText().toString();
                Date data = null;
                try {
                    data = new SimpleDateFormat("dd/MM/yyyy").parse(dt);
                    Log.i("eFinancasAndroid", "dt: " + dt);
                    transacao.setData(data);
                } catch (ParseException e) {
                    toast(e.getMessage());
                }
                transacao.setIdCategoria(0);
                transacao.setIdConta(idConta);
                debug("transação com idConta " + idConta);
                 toast(transacao.getId());
                if(transacao.getId() == 0){
                    transacaoDataSource.createTransacao(transacao);
                }else{
                    transacaoDataSource.updateTransacao(transacao);
                }

                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setClass(EditarTransacao.this,ListarTransacoesPorConta.class);
                startActivity(intent);

            }
        });

        txtData = (EditText)findViewById(R.id.txtDataET);
        txtData.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditarTransacao.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel() {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        txtData.setText(sdf.format(myCalendar.getTime()));
    }

    public void toast(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT);
    }

    public void toast(int intMsg){
        toast(String.valueOf(intMsg));
    }

    public void toast(long longMsg){
        toast(String.valueOf(longMsg));
    }

    public void debug(String msg){
        Log.d("eFinancasAndroid",msg);
    }
}
