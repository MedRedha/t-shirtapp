/**
 * Created by Amine on 12/24/15.
 */

(function () {
    angular.module('app')
        .controller('ClientController', clientController);
    clientController.$injector = ['Client', 'DateUtils','SweetAlert']
    function clientController(Client, DateUtils,SweetAlert) {
        var vm = this;
        vm.clients = [];
        vm.alerts=[];

        vm.loadAll = function () {
            Client.query(function (result) {
                vm.clients = result;
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
                    text: "The Client wont able to use his account",
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonColor: "#DD6B55",
                    confirmButtonText: "Yes, Block it"
                },
                function (isConfirm) {
                    if (isConfirm) {
                        item.status="BLOCKED"
                        Client.update(item,function (data, headers) {

                            vm.alerts.push({
                                type: 'success',
                                msg: 'Client ' + data.username + " : Status changed to " + data.status
                            });

                        }, function (httpResponse) {
                            console.log(httpResponse.headers('failure'));
                            vm.alerts.push({type: 'danger', msg: '' + httpResponse.headers('failure')});
                        })

                    }
                });
        }

        vm.activate=function(item){
            item.status="ACTIVATED"
            Client.update(item,function (data, headers) {

                vm.alerts.push({
                    type: 'success',
                    msg: 'Client ' + data.username + " : Status changed to " + data.status
                });

            }, function (httpResponse) {
                console.log(httpResponse.headers('failure'));
                vm.alerts.push({type: 'danger', msg: '' + httpResponse.headers('failure')});
            })
        }

        vm.isBlocked=function(status){
            return status==="BLOCKED";
        }
        vm.closeAlert = function (index) {
            vm.alerts.splice(index, 1);
        };


    }
})();
