function onError(message, callback){
    Swal({
        title: 'Oops...',
        text: message,
        type: 'error',
    }).then(() => {if (callback) callback()});
}