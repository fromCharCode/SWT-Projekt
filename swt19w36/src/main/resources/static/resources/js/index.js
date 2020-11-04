"use strict";

jQuery(document).ready(function($) {

    $('#uCheck').click(function () {
        if ($('#uButton').is(':disabled')){
            $('#uButton').removeAttr('disabled');
        } else {
            $('#uButton').attr('disabled', 'disabled');
        }
    });

    $('#cCheck').click(function () {
        if ($('#cButton').is(':disabled')){
            $('#cButton').removeAttr('disabled');
        } else {
            $('#cButton').attr('disabled', 'disabled');
        }
    });


});

function conf() {
    return confirm("Sind Sie sich sicher, dass Sie diesen Account löschen möchten?");
}

function confOrderDek() {
    return confirm("Sind Sie sich sicher, dass Sie diese Bestellung akzeptieren möchten?");
}

function confDelEmply() {
    return confirm("Sind Sie sich sicher, dass Sie diesen Mitarbeiter löschen möchten?");
}

function confMealDel() {
    return confirm("Sind Sie sich sicher, dass Sie dieses Gericht aus ihrer Bestellung entfernen möchten?");
}

function confDelOrg() {
    return confirm("Sind Sie sich sicher, dass Sie dieses Organisation sperren möchten?");
}

function confDelAcc() {
    return confirm("Sind Sie sich sicher, dass Sie diesen Kunden sperren möchten?");
}

function confDelStorage() {
    return confirm("Bitte nur löschen, wenn wirklich alle verdorbenen Zutaten aus dem Lager entnommen wurden!");
}

function fill(name) {
    document.getElementById("inputIngName").value = name;
}

function confDelIngredient() {
    return confirm("Sind Sie sicher, dass Sie die Zutat löschen möchten?");
}

function confDelMenu() {
    return confirm("Sind Sie sich sicher, dass Sie diesen Speiseplan löschen möchten?");
}

function inputError() {
    var x, y, z;
    x = document.getElementById("inputIngPrice").value;
    try {
        if (x == "") throw "empty";
        if (isNaN(x)) throw "not a number";
    } catch (err) {
        alert("Bitte den Preis so eingeben: 1.0")
    }
    y = document.getElementById("inputIngMinAmount").value;
    try {
        if (y == "") throw "empty";
        if (isNaN(y)) throw "not a number";
    } catch (err) {
        alert("Bitte die Mindestmenge so eingeben: 1.0")
    }
    z = document.getElementById("inputIngQuantity").value;
    try {
        if (z == "") throw "empty";
        if (isNaN(z)) throw "not a number";
    } catch (err) {
        alert("Bitte die Anzahl so eingeben: 1.0")
    }
}

