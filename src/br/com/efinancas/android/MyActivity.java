package br.com.efinancas.android;

import android.app.ListActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import br.com.efinancas.android.dao.Conta;
import br.com.efinancas.android.dao.ContaDataSource;

import java.util.List;

public class MyActivity extends ListActivity {

    private boolean doubleBackToExitPressedOnce = false;

    private ContaDataSource contaDataSource;

    List<Conta> contas;

    Button btnNovaConta;
    TextView lblValorTotal;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        contaDataSource = new ContaDataSource(this);
        contaDataSource.open();

        contas = contaDataSource.getAllcontas();

        ArrayAdapter<Conta> contaArrayAdapter = new ArrayAdapter<Conta>(this,
                android.R.layout.simple_list_item_1, contas);

        setListAdapter(contaArrayAdapter);

        btnNovaConta = (Button)findViewById(R.id.btnNovaConta);
        btnNovaConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyActivity.this,EditarConta.class);
                startActivity(intent);
            }
        });

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(MyActivity.this, ListarTransacoesPorConta.class);
                intent.putExtra("idConta", contas.get(Integer.valueOf(Long.toString(l))).getId());
                startActivity(intent);
            }
        });

        registerForContextMenu(getListView());
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        String[] menuItems = getResources().getStringArray(R.array.contextMenu);
        String menuItemName = menuItems[menuItemIndex];
        String listItemName = contas.get(info.position).getNome();
        Toast.makeText(MyActivity.this,String.format("Selected %s for item %s", menuItemName, listItemName),Toast.LENGTH_LONG).show();
        Intent intent = new Intent(MyActivity.this,EditarConta.class);
        intent.putExtra("idConta", contas.get(menuItemIndex).getId());
        startActivity(intent);

        return true;
    }

    @Override
    protected void onResume() {
        contaDataSource.open();
        super.onResume();

        this.doubleBackToExitPressedOnce = false;
    }

    @Override
    protected void onPause() {
        contaDataSource.close();
        super.onPause();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==android.R.id.list) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle(contas.get(info.position).getNome());
            String[] menuItems = getResources().getStringArray(R.array.contextMenu);
            for (int i = 0; i<menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Pressione voltar novamente para sair", Toast.LENGTH_SHORT).show();
    }
}
