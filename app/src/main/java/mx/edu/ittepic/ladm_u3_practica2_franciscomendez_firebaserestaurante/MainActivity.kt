package mx.edu.ittepic.ladm_u3_practica2_franciscomendez_firebaserestaurante

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.consulta.*

class MainActivity : AppCompatActivity() {
    var baseRemota = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        estado.setOnClickListener {
            descripcion.isEnabled = estado.isChecked
            precio.isEnabled = estado.isChecked
            cantidad.isEnabled = estado.isChecked
        }

        insertar.setOnClickListener {
            insertarRegistro()
        }
        consulta.setOnClickListener {
            consultaDatos()
        }
    }

    private fun consultaDatos() {
        var dialogo = Dialog(this)

        dialogo.setContentView(R.layout.consulta)

        var valor = dialogo.findViewById<EditText>(R.id.valor)
        var posicion = dialogo.findViewById<Spinner>(R.id.clave)
        var buscar = dialogo.findViewById<Button>(R.id.buscar)
        var cerrar = dialogo.findViewById<Button>(R.id.cerrar)

        dialogo.show()
        cerrar.setOnClickListener {
            dialogo.dismiss()
        }
        buscar.setOnClickListener {
            if(valor.text.isEmpty()){
                Toast.makeText(this,"DEBES PONER VALOR PARA BUSCAR", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            buscarDato(valor.text.toString(),posicion.selectedItemPosition)
            dialogo.dismiss()
        }
    }

    private fun buscarDato(valor: String, clave: Int) {
        when(clave){
            0->{consultaNombre(valor)}
            1->{consultaCelular(valor)}
            2->{consultaDomicilio(valor)}
        }
    }

    private fun consultaDomicilio(valor: String){
        baseRemota.collection("restaurante")
            .whereEqualTo("domicilio",valor)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException !=null){
                    resultado.setText("ERROR NO HAY CONEXION")
                    return@addSnapshotListener
                }
                var res = ""
                for(document in querySnapshot!!){
                    res += "ID"+document.id+"\nNombre: "+document.getString("nombre")+
                            "\nDomicilio: "+document.getString("domicilio")+
                            "\nCelular: "+document.getString("celular")+
                            "\nDescripcion: "+document.get("pedido.descripcion")+
                            "\nPrecio: "+document.get("pedido.precio")+
                            "\nCantidad: "+document.get("pedido.cantidad")+
                            "\nEstado: "+document.get("pedido.estado")
                }
                if(res.indexOf("null")>=0) {
                    res = res.substring(0,res.indexOf("pedido"))
                }
                resultado.setText(res)
            }
    }

    private fun consultaCelular(valor: String) {
        baseRemota.collection("restaurante")
            .whereEqualTo("celular",valor)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException !=null){
                    resultado.setText("ERROR NO HAY CONEXION")
                    return@addSnapshotListener
                }
                var res = ""
                for(document in querySnapshot!!){
                    res += "ID"+document.id+"\nNombre: "+document.getString("nombre")+
                            "\nDomicilio: "+document.getString("domicilio")+
                            "\nCelular: "+document.getString("celular")+
                            "\nDescripcion: "+document.get("pedido.descripcion")+
                            "\nPrecio: "+document.get("pedido.precio")+
                            "\nCantidad: "+document.get("pedido.cantidad")+
                            "\nEstado: "+document.get("pedido.estado")
                }
                if(res.indexOf("null")>=0) {
                    res = res.substring(0,res.indexOf("pedido"))
                }
                resultado.setText(res)
            }
    }

    private fun consultaNombre(valor: String) {
        baseRemota.collection("restaurante")
            .whereEqualTo("nombre",valor)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException !=null){
                    resultado.setText("ERROR NO HAY CONEXION")
                    return@addSnapshotListener
                }
                var res = ""
                for(document in querySnapshot!!){
                    res += "ID"+document.id+"\nNombre: "+document.getString("nombre")+
                            "\nDomicilio: "+document.getString("domicilio")+
                            "\nCelular: "+document.getString("celular")+
                            "\nDescripcion: "+document.get("pedido.descripcion")+
                            "\nPrecio: "+document.get("pedido.precio")+
                            "\nCantidad: "+document.get("pedido.cantidad")+
                            "\nEstado: "+document.get("pedido.estado")
                }
                if(res.indexOf("null")>=0) {
                    res = res.substring(0,res.indexOf("pedido"))
                }
                resultado.setText(res)
            }
    }

    private fun insertarRegistro() {
        var data = hashMapOf(
            "nombre" to nombre.text.toString(),
            "domicilio" to domicilio.text.toString(),
            "celular" to celular.text.toString()
        )
        baseRemota.collection("restaurante")
            .add(data)
            .addOnSuccessListener {
                if (estado.isChecked==true) {
                    var pedido = hashMapOf(
                        "descripcion" to descripcion.text.toString(),
                        "precio" to precio.text.toString().toFloat(),
                        "cantidad" to cantidad.text.toString().toInt(),
                        "estado" to estado.text.toString().toBoolean()

                    )
                    baseRemota.collection("restaurante")
                        .document(it.id)
                        .update("pedido", pedido as Map<String, Any>)
                }
                Toast.makeText(this,"SE AGREGO CORRECTAMENTE",Toast.LENGTH_LONG)
                    .show()
                nombre.setText(""); domicilio.setText(""); celular.setText(""); descripcion.setText("");precio.setText("");cantidad.setText("");
            }
            .addOnFailureListener {
                Toast.makeText(this, "ERROR NO SE CAPTURO", Toast.LENGTH_LONG)
                    .show()
            }
    }

}
