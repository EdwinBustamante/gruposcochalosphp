package com.edwinbustamante.gruposcochalos.Persistencia;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.edwinbustamante.gruposcochalos.domain.GrupoMusical;

import java.util.List;

/**
 * Created by EDWIN on 4/6/2018.
 */
@Dao
public interface GrupoDAO {
@Insert
public void insert(GrupoMusical grupoMusical);
@Delete
public void delete(GrupoMusical grupoMusical);
@Query("SELECT * FROM favoritos")
public List<GrupoMusical> getFavoritos();
}
