/**
 * Created by Amine on 12/24/15.
 */

(function () {
    angular.module('app')
        .controller('UserController', userController);
    userController.$injector = ['User', 'DateUtils','ngDialog','SweetAlert']
    function userController(User, DateUtils,SweetAlert) {
        var vm = this;
        vm.users = [];

        vm.loadAll = function () {
            User.query(function (result) {
                vm.users = result;
            });
        };


        vm.loadAll();


        vm.convertDate = function (i) {
            console.log(i)
            return DateUtils.convertDateTimeFromServer(i);
        }



        vm.block=function(item){

            SweetAlert.swal({
                    title: "Are you sure?",
                    text: "The user wont able to use his account",
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonColor: "#DD6B55",
                    confirmButtonText: "Yes, Block it"
                },
                function (isConfirm) {
                    if (isConfirm) {
                        item.status="BLOCKED"
                        User.update(item,onOK,onFail)

                    }
                });
        }

        vm.activate=function(item){
            item.status="ACTIVATED"
            User.update(item,onOK,onFail)
        }

        vm.isBlocked=function(status){
            return status==="BLOCKED";
        }

        vm.delete=function(i) {
            SweetAlert.swal({
                    title: "Are you sure?",
                    text: "The wont able to recover the user: "+ i.firstName +" "+ i.lastName+" account again ",
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonColor: "#DD6B55",
                    confirmButtonText: "Yes, delete it"
                },
                function (isConfirm) {
                    if (isConfirm) {
                        User.delete({id: i.id},
                            function () {
                                vm.loadAll();
                                Materialize.toast("Deleted", 4000);
                            });

                    }
                });

        }

        function onOK (result) {
            Materialize.toast("StatusChanged", 4000);

        }

        function onFail (result) {
            Materialize.toast("Error", 4000);
        }

    }
})();
