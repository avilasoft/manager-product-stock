import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './project-item-list-item.reducer';
import { IProjectItemListItem } from 'app/shared/model/project-item-list-item.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ProjectItemListItem = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(props.location, ITEMS_PER_PAGE, 'id'), props.location.search)
  );

  const projectItemListItemList = useAppSelector(state => state.projectItemListItem.entities);
  const loading = useAppSelector(state => state.projectItemListItem.loading);
  const totalItems = useAppSelector(state => state.projectItemListItem.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      })
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (props.location.search !== endURL) {
      props.history.push(`${props.location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(props.location.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [props.location.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const { match } = props;

  return (
    <div>
      <h2 id="project-item-list-item-heading" data-cy="ProjectItemListItemHeading">
        <Translate contentKey="managerProductStockApp.projectItemListItem.home.title">Project Item List Items</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="managerProductStockApp.projectItemListItem.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="managerProductStockApp.projectItemListItem.home.createLabel">
              Create new Project Item List Item
            </Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {projectItemListItemList && projectItemListItemList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="managerProductStockApp.projectItemListItem.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                {/*
                <th className="hand" onClick={sort('code')}>
                  <Translate contentKey="managerProductStockApp.projectItemListItem.code">Code</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="managerProductStockApp.projectItemListItem.name">Name</Translate> <FontAwesomeIcon icon="sort" />
        </th>*/}
                <th>
                  <Translate contentKey="managerProductStockApp.projectItemListItem.item">Item</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('dimension')}>
                  <Translate contentKey="managerProductStockApp.projectItemListItem.dimension">Dimension</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="managerProductStockApp.projectItemListItem.unit">Unit</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('quantity')}>
                  <Translate contentKey="managerProductStockApp.projectItemListItem.quantity">Quantity</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="managerProductStockApp.projectItemListItem.providerItem">Provider Item</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="managerProductStockApp.projectItemListItem.providerItemCost">Provider Item Cost</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="managerProductStockApp.projectItemListItem.providerItemUnit">Provider Item Unit</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="managerProductStockApp.projectItemListItem.projectItemList">Project Item List</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {projectItemListItemList.map((projectItemListItem, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${projectItemListItem.id}`} color="link" size="sm">
                      {projectItemListItem.id}
                    </Button>
                  </td>
                  {/*
                  <td>{projectItemListItem.code}</td>
              <td>{projectItemListItem.name}</td>*/}
                  <td>
                    {projectItemListItem.item ? (
                      <Link to={`item/${projectItemListItem.item.id}`}>{projectItemListItem.item.name}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>{projectItemListItem.dimension}</td>
                  <td>
                    {projectItemListItem.unit ? (
                      <Link to={`unit/${projectItemListItem.unit.id}`}>{projectItemListItem.unit.name}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>{projectItemListItem.quantity}</td>
                  <td>
                    {projectItemListItem.providerItem ? (
                      <Link to={`provider-item/${projectItemListItem.providerItem.id}`}>{projectItemListItem.providerItem.name}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>{projectItemListItem.providerItem ? projectItemListItem.providerItem.cost : ''}</td>
                  <td>{projectItemListItem.providerItem ? projectItemListItem.providerItem.unit.name : ''}</td>
                  <td>
                    {projectItemListItem.projectItemList ? (
                      <Link to={`project-item-list/${projectItemListItem.projectItemList.id}`}>
                        {projectItemListItem.projectItemList.name}
                      </Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${projectItemListItem.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${projectItemListItem.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${projectItemListItem.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="managerProductStockApp.projectItemListItem.home.notFound">No Project Item List Items found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={projectItemListItemList && projectItemListItemList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default ProjectItemListItem;
