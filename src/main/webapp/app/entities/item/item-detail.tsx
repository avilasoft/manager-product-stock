import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './item.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ItemDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const itemEntity = useAppSelector(state => state.item.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="itemDetailsHeading">
          <Translate contentKey="managerProductStockApp.item.detail.title">Item</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{itemEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="managerProductStockApp.item.name">Name</Translate>
            </span>
          </dt>
          <dd>{itemEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="managerProductStockApp.item.description">Description</Translate>
            </span>
          </dt>
          <dd>{itemEntity.description}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="managerProductStockApp.item.type">Type</Translate>
            </span>
          </dt>
          <dd>{itemEntity.type}</dd>
          <dt>
            <span id="image">
              <Translate contentKey="managerProductStockApp.item.image">Image</Translate>
            </span>
          </dt>
          <dd>
            {itemEntity.image ? (
              <div>
                {itemEntity.imageContentType ? (
                  <a onClick={openFile(itemEntity.imageContentType, itemEntity.image)}>
                    <img src={`data:${itemEntity.imageContentType};base64,${itemEntity.image}`} style={{ maxHeight: '30px' }} />
                  </a>
                ) : null}
                <span>
                  {itemEntity.imageContentType}, {byteSize(itemEntity.image)}
                </span>
              </div>
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/item" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/item/${itemEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ItemDetail;
