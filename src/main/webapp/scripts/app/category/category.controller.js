/**
 * Created by Amine on 12/24/15.
 */

(function () {
    angular.module('app')
        .controller('CategoryController', categoryController);
    categoryController.$injector = ['Category', 'SweetAlert', 'ParseLinks']
    function categoryController(Category, SweetAlert, ParseLinks) {

        var vm = this;
        vm.categorys = [];
        vm.alerts = [];
        vm.currentPage = 1;

        vm.loadAll = function () {
            Category.query({page: vm.currentPage - 1, size: 5}, function (result, headers) {
                vm.totalItems = headers('X-Total-Count');
                vm.categorys = result;
            });
        };
        vm.ItemPerPage = 5;
        vm.loadAll();
        vm.pageChanged = function () {
            vm.loadAll()
        };


       


       
       


        vm.closeAlert = function (index) {
            vm.alerts.splice(index, 1);
        };



        vm.delete=function(i) {
            SweetAlert.swal({
                    title: "Are you sure?",
                    text: "The wont able to recover the Category: "+ i.name +" ",
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonColor: "#DD6B55",
                    confirmButtonText: "Yes, delete it"
                },
                function (isConfirm) {
                    if (isConfirm) {
                        Category.delete({id: i.id},
                            function () {
                                vm.loadAll();
                                vm.alerts.push({type: 'success', msg: 'Category Deleted'});

                            });

                    }
                });

        }

    }
})();
