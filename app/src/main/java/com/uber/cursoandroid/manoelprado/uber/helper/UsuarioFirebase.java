package com.uber.cursoandroid.manoelprado.uber.helper;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.uber.cursoandroid.manoelprado.uber.activity.PassageiroActivity;
import com.uber.cursoandroid.manoelprado.uber.activity.RequisicoesActivity;
import com.uber.cursoandroid.manoelprado.uber.config.ConfiguracaoFirebase;
import com.uber.cursoandroid.manoelprado.uber.model.Usuario;

public class UsuarioFirebase {

    public static  FirebaseUser getUsuarioAtual (){  //RECUPERA APENAS O USUARIO ATUAL!
        FirebaseAuth usuario = ConfiguracaoFirebase.getFirebaseAutenticacao();
        return usuario.getCurrentUser();  //DO TIPO FirebaseUser
    }

    public static Usuario getDadosUsuarioLogado(){  //RECUPERA OS DADOS DO USUARIO LOGADO (tipo Usuario!!)
        FirebaseUser firebaseUser = getUsuarioAtual();
        Usuario usuario = new Usuario();
        usuario.setId(firebaseUser.getUid());
        usuario.setEmail(firebaseUser.getEmail());
        usuario.setNome(firebaseUser.getDisplayName());

        return usuario;
    }

    public static boolean atualizarNomeUsuario (String nome){

        try {

            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nome)
                    .build();
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if( !task.isSuccessful() ) {
                        Log.d("Perfil", "Erro ao atualizar nome de perfil.");
                    }
                }
            });

            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    public static void redirecionaUsuarioLogado(final Activity activity){ //precisa passar a activity!

        FirebaseUser user = getUsuarioAtual();
        if (user !=null){ //se o usuario estiver logado!!
            DatabaseReference usuariosRef = ConfiguracaoFirebase.getFirebaseDatabase()
                    .child("usuarios")
                    .child(getIdentificadorUsuario());
            usuariosRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Usuario usuario = dataSnapshot.getValue(Usuario.class);//recupera o objeto usuario

                    String tipoUsuario = usuario.getTipo();
                    if(tipoUsuario.equals("M")){ //redireciona par a atela de motorista
                        Intent i = new Intent(activity, RequisicoesActivity.class);
                        activity.startActivity(i);
                    }else { //redireciona par a atela de passageiro
                        Intent i = new Intent(activity, PassageiroActivity.class);
                        activity.startActivity(i);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


    }

    public static String getIdentificadorUsuario(){
        return getUsuarioAtual().getUid();
    }

}
