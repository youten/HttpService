// myhttp
var API_DATASTORE = "./ds";
var KEY_DEFAULT = "default";

var MyHttp;
(function() {
    "use strict";
    MyHttp = function() {
    }

    MyHttp.get = function(key, callback) {
        console.log('get key=' + key);

        $.ajax({
            url: API_DATASTORE,
            type: 'GET',
            dataType: 'text',
            data: 'key=' + key,
            timeout: 10000,
            success: function(data) {
                console.log('success:' + data);
                callback(data);
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                console.log('error:' + textStatus);
            }
        });
    };

    MyHttp.post = function(key, value, callback) {
        "use strict";
        console.log('post key=' + key + ' value=' + value);

        $.ajax({
            url: API_DATASTORE + '?key=' + key,
            type: 'POST',
            contentType: 'application/json',
            dataType: 'text',
            data: value,
            timeout: 10000,
            success: function(data) {
                console.log('success:' + data);
                callback(data);
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                console.log('error:' + textStatus);
            }
        });
    };

})();

$(document).ready(function() {
    console.log('myhttp ready');
    $('#textarea1').text('{"hige":"hige1"}');


    $('#getbutton').on('click', function() {
        MyHttp.get(KEY_DEFAULT, function(result) {
            console.log('callback:' + result);
            $('#textarea1').val(result);
            var obj = JSON.parse(result);
            console.log('hige=' + obj.hige);
            console.log('hige1=' + obj.hige1);
            console.log('hige2=' + obj.hige2);
        });
    });

    $('#postbutton').on('click', function() {
        MyHttp.post(KEY_DEFAULT, $('#textarea1').val(), function(result) {
            console.log('callback:' + result);
        });
    });
});