(function() {
    'use strict';

    angular
        .module('petStoreApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('pet', {
            parent: 'entity',
            url: '/pet',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'petStoreApp.pet.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pet/pets.html',
                    controller: 'PetController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('pet');
                    $translatePartialLoader.addPart('petStatus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('pet-detail', {
            parent: 'entity',
            url: '/pet/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'petStoreApp.pet.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pet/pet-detail.html',
                    controller: 'PetDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('pet');
                    $translatePartialLoader.addPart('petStatus');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Pet', function($stateParams, Pet) {
                    return Pet.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('pet.new', {
            parent: 'pet',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pet/pet-dialog.html',
                    controller: 'PetDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('pet', null, { reload: true });
                }, function() {
                    $state.go('pet');
                });
            }]
        })
        .state('pet.edit', {
            parent: 'pet',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pet/pet-dialog.html',
                    controller: 'PetDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Pet', function(Pet) {
                            return Pet.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('pet', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('pet.delete', {
            parent: 'pet',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pet/pet-delete-dialog.html',
                    controller: 'PetDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Pet', function(Pet) {
                            return Pet.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('pet', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
