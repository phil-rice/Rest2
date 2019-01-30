function parse(t) {
    return JSON.parse(t);
}

function getL(lensName, t) {
    var lens = eval(lensName)();
    return lens.get(t)
}

function setL(lensName, t, v) {
    return eval(lensName)().set(t, v)
}

function getFromList(list, n) {
    return list[n];
}

function sizeOfList(list) {
    return list.length;
}

function setInList(list, n, item) {
    var newList = list.splice();
    newList[n] = item;
    return newList;
}

function makeArray() {
    var result = [];
    for (var i = 0; i < arguments.length; i++) {
        result.push(arguments[i])
    }
    return result
}

function shallowCopy(t) {
    var result = {};
    for (var key in t) {
        result[key] = t[key];
    }
    return result;
}

function lens(field) {
    return {
        "get": function (t) {
            return t[field];
        },
        "set": function (t, v) {
            var copy = shallowCopy(t);
            copy[field] = v;
            return copy
        }
    };
}

function identityLens() {
    return {
        get: function (t) {
            return t;
        },
        set: function (t, b) {
            return b;
        }
    }
}

function itemAsListLens(){
    return {
        get: function(t){
            return [t];
        },
        set: function(t,b){
            return b[0];
        }
    }
}

function lensForFirstItemInList() {
    return {
        "get": function (list) {
            return list[0];
        },
        "set": function (list, item) {
            var newArray = list.slice();
            newArray[0] = item;
            return newArray
        }
    }
}

function compose(l1, l2) {
    return {
        "get": function (t) {
            return l2.get(l1.get(t));
        },
        "set": function (t, v) {
            return l1.set(t, l2.set(l1.get(t), v));
        }
    }
};

function getField(main, field) {
    return main[field];
};

function render_json(t) {
    return JSON.stringify(t)
};

function render_pretty(t) {
    return JSON.stringify(t, null, 2)
};
