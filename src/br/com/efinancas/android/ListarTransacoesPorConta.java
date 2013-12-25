package br.com.efinancas.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import br.com.efinancas.android.dao.Transacao;
import br.com.efinancas.android.dao.TransacaoDataSource;

import java.util.List;

/**
 * @author: Misael Ferreira
 * Date: 15/12/13
 * Time: 11:58
 */
public class ListarTransacoesPorConta extends Activity {

    TransacaoDataSource transacaoDataSource;
    List<Transacao> transacoes;
    Button btnNovaTransacao;

    LazyAdapter adapter;
    ListView list;
    int idConta = 0;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listar_transacoes_por_conta);

        transacaoDataSource = new TransacaoDataSource(this);
        transacaoDataSource.open();

        Intent i = getIntent();
        long tmpIdConta = getIntent().getLongExtra("idConta",0);
        idConta = Integer.valueOf(String.valueOf(tmpIdConta));
        log("idConta: " + idConta);
        transacoes = transacaoDataSource.listTransacaoByIdConta(idConta);

        //transacoes = transacaoDataSource.getAlltransacaos();

        list = (ListView)findViewById(R.id.listTransacoes_ltc);

        adapter = new LazyAdapter(this,transacoes);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(ListarTransacoesPorConta.this, EditarTransacao.class);
                int indexTransacao = Integer.parseInt(Long.toString(l));
                long idTransacao = transacoes.get(indexTransacao).getId();
                intent.putExtra("idTransacao", idTransacao);
                if(idConta != 0){
                    intent.putExtra("idConta", idConta);
                }
                startActivity(intent);
            }
        });

        btnNovaTransacao = (Button)findViewById(R.id.add);
        btnNovaTransacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListarTransacoesPorConta.this, EditarTransacao.class);
                intent.putExtra("idConta", idConta);
                startActivity(intent);
            }
        });
    }

    private void log(String msg){
        Log.d("eFinancasAndroid",msg);
    }
}
