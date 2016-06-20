(function() {
    'use strict';

    angular
        .module('petStoreApp')
        .controller('PhotoUrlDeleteController',PhotoUrlDeleteController);

    PhotoUrlDeleteController.$inject = ['$uibModalInstance', 'entity', 'PhotoUrl'];

    function PhotoUrlDeleteController($uibModalInstance, entity, PhotoUrl) {
        var vm = this;

        vm.photoUrl = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PhotoUrl.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
