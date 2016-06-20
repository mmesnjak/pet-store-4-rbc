(function() {
    'use strict';

    angular
        .module('petStoreApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('photo-url', {
            parent: 'entity',
            url: '/photo-url',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'petStoreApp.photoUrl.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/photo-url/photo-urls.html',
                    controller: 'PhotoUrlController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('photoUrl');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('photo-url-detail', {
            parent: 'entity',
            url: '/photo-url/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'petStoreApp.photoUrl.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/photo-url/photo-url-detail.html',
                    controller: 'PhotoUrlDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('photoUrl');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'PhotoUrl', function($stateParams, PhotoUrl) {
                    return PhotoUrl.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('photo-url.new', {
            parent: 'photo-url',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/photo-url/photo-url-dialog.html',
                    controller: 'PhotoUrlDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                url: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('photo-url', null, { reload: true });
                }, function() {
                    $state.go('photo-url');
                });
            }]
        })
        .state('photo-url.edit', {
            parent: 'photo-url',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/photo-url/photo-url-dialog.html',
                    controller: 'PhotoUrlDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PhotoUrl', function(PhotoUrl) {
                            return PhotoUrl.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('photo-url', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('photo-url.delete', {
            parent: 'photo-url',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/photo-url/photo-url-delete-dialog.html',
                    controller: 'PhotoUrlDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PhotoUrl', function(PhotoUrl) {
                            return PhotoUrl.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('photo-url', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
