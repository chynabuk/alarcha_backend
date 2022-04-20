package com.project.alarcha.service;

import java.util.List;

public interface Convertions<T, G> {
    List<T> convertToModels(List<G> gList);
    T toModel(G g);
}
