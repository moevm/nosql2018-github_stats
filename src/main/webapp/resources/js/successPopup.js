function onSuccess(message){
    Swal({
        title: 'Success',
        text: message,
        type: 'success',
        showConfirmButton: false,
        timer: 500
    });
}