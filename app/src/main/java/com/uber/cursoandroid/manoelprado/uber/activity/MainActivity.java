package com.uber.cursoandroid.manoelprado.uber.activity;

import com.google.firebase.auth.FirebaseAuth;
import com.uber.cursoandroid.manoelprado.uber.R;
import com.uber.cursoandroid.manoelprado.uber.config.ConfiguracaoFirebase;
import com.uber.cursoandroid.manoelprado.uber.helper.Permissoes;
import com.uber.cursoandroid.manoelprado.uber.helper.UsuarioFirebase;
import com.uber.cursoandroid.manoelprado.uber.model.Usuario;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private String[] permissoes = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        //validar permissoes
        Permissoes.validarPermissoes(permissoes, this, 1);

        /*autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signOut();*///isso é so pra se eu quiser deslogar o usuario ao entrar
    }

    public void abrirTelaLogin(View view){
        startActivity(new Intent(this, LoginActivity.class));

    }
    public void abrirTelaCadastro(View view){
        startActivity(new Intent(this, CadastroActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        UsuarioFirebase.redirecionaUsuarioLogado(MainActivity.this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for(int permissaoResultado : grantResults){
            if(permissaoResultado == PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();
            }
        }
    }
    private void alertaValidacaoPermissao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this );
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar o app é necessário aceitar as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
